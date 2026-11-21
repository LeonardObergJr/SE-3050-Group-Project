const API_BASE = "/api";

const params = new URLSearchParams(window.location.search);
const productId = params.get("productId");

let selectedProduct = null;

const selectedProductContainer = document.getElementById("selectedProduct");
const orderForm = document.getElementById("orderForm");
const quantityInput = document.getElementById("quantity");
const orderTotal = document.querySelector("#orderTotal strong");
const orderMessage = document.getElementById("orderMessage");

function escapeHtml(value) {
return String(value ?? "")
.replaceAll("&", "&amp;")
.replaceAll("<", "&lt;")
.replaceAll(">", "&gt;")
.replaceAll('"', "&quot;")
.replaceAll("'", "&#39;");
}

async function loadProduct() {
if (!productId) {
showProductError();
return;
}

try {
    const response = await fetch(`${API_BASE}/products/${productId}`);

    if (!response.ok) {
        throw new Error("Product not found");
    }

    selectedProduct = await response.json();

    displayProduct();
    updateTotal();
} catch (error) {
    showProductError();
}

}

function displayProduct() {
if (!selectedProductContainer || !selectedProduct) {
return;
}

selectedProductContainer.innerHTML = `
    <div class="selected-product-icon">🥬</div>
    <span class="section-label">SELECTED PRODUCT</span>
    <h2>${escapeHtml(selectedProduct.name)}</h2>
    <p class="selected-vendor">
        ${escapeHtml(selectedProduct.vendorName)}
    </p>
    <p>${escapeHtml(selectedProduct.categoryName)}</p>
    <p>${escapeHtml(selectedProduct.description)}</p>
    <div class="selected-price">
        $${Number(selectedProduct.price).toFixed(2)}
    </div>
    <div class="stock-info">
        ${selectedProduct.stockQuantity} units available
    </div>
`;

if (quantityInput) {
    quantityInput.max = selectedProduct.stockQuantity;
}

}

function updateTotal() {
if (!selectedProduct || !quantityInput || !orderTotal) {
return;
}

let quantity = Number(quantityInput.value);

if (!Number.isInteger(quantity) || quantity < 1) {
    quantity = 1;
}

orderTotal.textContent =
    `$${(Number(selectedProduct.price) * quantity).toFixed(2)}`;

}

function showMessage(message, type) {
if (!orderMessage) {
return;
}

orderMessage.textContent = message;
orderMessage.className = `order-message ${type}`;

}

function validateOrder() {
const firstName = document.getElementById("firstName");
const lastName = document.getElementById("lastName");
const email = document.getElementById("email");
const phone = document.getElementById("phone");

if (!firstName.value.trim()) {
    showMessage("First name is required.", "error");
    firstName.focus();
    return false;
}

if (!lastName.value.trim()) {
    showMessage("Last name is required.", "error");
    lastName.focus();
    return false;
}

if (!email.value.trim() || !email.validity.valid) {
    showMessage("Enter a valid email address.", "error");
    email.focus();
    return false;
}

if (phone.value.trim() && !/^[0-9+\-\s()]{7,30}$/.test(phone.value.trim())) {
    showMessage("Enter a valid phone number.", "error");
    phone.focus();
    return false;
}

const quantity = Number(quantityInput.value);

if (!Number.isInteger(quantity) || quantity < 1) {
    showMessage("Quantity must be at least 1.", "error");
    quantityInput.focus();
    return false;
}

if (quantity > selectedProduct.stockQuantity) {
    showMessage(
        `Only ${selectedProduct.stockQuantity} units are available.`,
        "error"
    );
    quantityInput.focus();
    return false;
}

return true;

}

async function submitOrder(event) {
event.preventDefault();

if (!selectedProduct) {
    showMessage("Product information is unavailable.", "error");
    return;
}

if (!validateOrder()) {
    return;
}

const submitButton = orderForm.querySelector("button[type='submit']");
const firstName = document.getElementById("firstName").value.trim();
const lastName = document.getElementById("lastName").value.trim();
const email = document.getElementById("email").value.trim();
const phone = document.getElementById("phone").value.trim();
const quantity = Number(quantityInput.value);

const requestData = {
    customer: {
        firstName,
        lastName,
        email,
        phone
    },
    items: [
        {
            productId: Number(productId),
            quantity
        }
    ]
};

submitButton.disabled = true;
submitButton.textContent = "Processing...";

try {
    const response = await fetch(`${API_BASE}/orders`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(requestData)
    });

    const result = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(
            result?.message || "Unable to place the order."
        );
    }

    showMessage(
        `Order #${result.orderId} was placed successfully.`,
        "success"
    );

    orderForm.reset();
    quantityInput.value = 1;
    updateTotal();
} catch (error) {
    showMessage(
        error.message || "Something went wrong while placing the order.",
        "error"
    );
} finally {
    submitButton.disabled = false;
    submitButton.textContent = "Place Pre-Order";
}

}

function showProductError() {
if (!selectedProductContainer) {
return;
}

selectedProductContainer.innerHTML = `
    <div class="product-not-found">
        <h2>Product not found</h2>
        <a href="index.html#products">Return to Products</a>
    </div>
`;

if (orderForm) {
    orderForm.style.display = "none";
}

}

document.addEventListener("DOMContentLoaded", () => {
loadProduct();

if (quantityInput) {
    quantityInput.addEventListener("input", updateTotal);
    quantityInput.addEventListener("change", updateTotal);
}

if (orderForm) {
    orderForm.addEventListener("submit", submitOrder);
}

});