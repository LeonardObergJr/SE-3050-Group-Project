const API_BASE = "/api";

const marketsContainer = document.getElementById("marketContainer");
const productsContainer = document.getElementById("productContainer");
const productSearch = document.getElementById("searchInput");
const searchButton = document.getElementById("searchButton");

function escapeHtml(value) {
return String(value ?? "")
.replaceAll("&", "&amp;")
.replaceAll("<", "&lt;")
.replaceAll(">", "&gt;")
.replaceAll('"', "&quot;")
.replaceAll("'", "&#39;");
}

async function loadMarkets() {
if (!marketsContainer) {
return;
}

try {
    const response = await fetch(`${API_BASE}/markets`);

    if (!response.ok) {
        throw new Error("Unable to load markets");
    }

    const markets = await response.json();

    if (!markets.length) {
        marketsContainer.innerHTML = `
            <div class="no-products">
                No markets are currently available.
            </div>
        `;
        return;
    }

    marketsContainer.innerHTML = markets.map(market => `
        <div class="market-card">
            <div class="market-icon">🌱</div>
            <h3>${escapeHtml(market.name)}</h3>
            <p>${escapeHtml(market.address)}</p>
            <p>${escapeHtml(market.city)}</p>
            <span>${escapeHtml(market.operatingDays)}</span>
            <a href="market.html?id=${market.marketId}" class="view-button">
                View Market
            </a>
        </div>
    `).join("");
} catch (error) {
    marketsContainer.innerHTML = `
        <div class="no-products">
            Unable to load markets. Please try again later.
        </div>
    `;
}

}

async function loadProducts(search = "") {
if (!productsContainer) {
return;
}

try {
    const url = search
        ? `${API_BASE}/products?search=${encodeURIComponent(search)}`
        : `${API_BASE}/products`;

    const response = await fetch(url);

    if (!response.ok) {
        throw new Error("Unable to load products");
    }

    const products = await response.json();

    if (!products.length) {
        productsContainer.innerHTML = `
            <div class="no-products">
                No products found.
            </div>
        `;
        return;
    }

    productsContainer.innerHTML = products.map(product => `
        <div class="product-card">
            <div class="product-icon">🥬</div>
            <span class="product-category">
                ${escapeHtml(product.categoryName)}
            </span>
            <h3>${escapeHtml(product.name)}</h3>
            <p>${escapeHtml(product.vendorName)}</p>
            <p>${escapeHtml(product.marketName ?? "Market")}</p>
            <div class="product-bottom">
                <strong>$${Number(product.price).toFixed(2)}</strong>
                <a href="order.html?productId=${product.productId}" class="order-button">
                    Pre-Order
                </a>
            </div>
        </div>
    `).join("");
} catch (error) {
    productsContainer.innerHTML = `
        <div class="no-products">
            Unable to load products. Please try again later.
        </div>
    `;
}

}

let searchTimer;

function handleProductSearch() {
clearTimeout(searchTimer);

searchTimer = setTimeout(() => {
    loadProducts(productSearch.value.trim());
}, 300);

}

function searchProducts() {
clearTimeout(searchTimer);
loadProducts(productSearch.value.trim());
}

document.addEventListener("DOMContentLoaded", () => {
loadMarkets();
loadProducts();

if (productSearch) {
    productSearch.addEventListener("input", handleProductSearch);
}

if (searchButton) {
    searchButton.addEventListener("click", searchProducts);
}

});