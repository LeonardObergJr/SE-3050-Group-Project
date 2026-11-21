const API_BASE = "/api";

const params = new URLSearchParams(window.location.search);
const marketId = params.get("id");

const marketDetails = document.getElementById("marketDetails");
const vendorsContainer = document.getElementById("vendorContainer");

function escapeHtml(value) {
return String(value ?? "")
.replaceAll("&", "&amp;")
.replaceAll("<", "&lt;")
.replaceAll(">", "&gt;")
.replaceAll('"', "&quot;")
.replaceAll("'", "&#39;");
}

async function loadMarket() {
if (!marketDetails || !marketId) {
showMarketError();
return;
}

try {
    const response = await fetch(`${API_BASE}/markets/${marketId}`);

    if (!response.ok) {
        throw new Error("Market not found");
    }

    const market = await response.json();

    marketDetails.innerHTML = `
        <div class="market-detail-icon">🌱</div>
        <div class="market-detail-content">
            <span class="section-label">FARMERS MARKET</span>
            <h1>${escapeHtml(market.name)}</h1>
            <p>
                ${escapeHtml(market.address)},
                ${escapeHtml(market.city)}
            </p>
            <div class="market-info">
                <div>
                    <strong>Open Days</strong>
                    <span>${escapeHtml(market.operatingDays)}</span>
                </div>
                <div>
                    <strong>Market Information</strong>
                    <span>${escapeHtml(market.description)}</span>
                </div>
            </div>
        </div>
    `;

    await loadVendors();
} catch (error) {
    showMarketError();
}

}

async function loadVendors() {
if (!vendorsContainer) {
return;
}

try {
    const response = await fetch(
        `${API_BASE}/markets/${marketId}/vendors`
    );

    if (!response.ok) {
        throw new Error("Unable to load vendors");
    }

    const vendors = await response.json();

    if (!vendors.length) {
        vendorsContainer.innerHTML = `
            <div class="no-products">
                No vendors are registered for this market.
            </div>
        `;
        return;
    }

    vendorsContainer.innerHTML = vendors.map(vendor => `
        <div class="vendor-card">
            <div class="vendor-icon">👨‍🌾</div>
            <div class="vendor-content">
                <h3>${escapeHtml(vendor.businessName)}</h3>
                <p>Contact: ${escapeHtml(vendor.contactName)}</p>
                <p>${escapeHtml(vendor.email)}</p>
                <p>${escapeHtml(vendor.phone)}</p>
                <span>Booth ${escapeHtml(vendor.boothNumber)}</span>
            </div>
        </div>
    `).join("");
} catch (error) {
    vendorsContainer.innerHTML = `
        <div class="no-products">
            Unable to load vendors.
        </div>
    `;
}

}

function showMarketError() {
if (marketDetails) {
marketDetails.innerHTML = `
    <div class="no-market">
        <h2>Market not found</h2>
        <a href="index.html">Return to Markets</a>
    </div>
`;
}

if (vendorsContainer) {
    vendorsContainer.innerHTML = "";
}

}

document.addEventListener("DOMContentLoaded", loadMarket);