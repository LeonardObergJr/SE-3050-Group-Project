package com.example.demo.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MarketVendorId implements Serializable {

    private Integer marketId;
    private Integer vendorId;

    public MarketVendorId() {}

    public MarketVendorId(Integer marketId, Integer vendorId) {
        this.marketId = marketId;
        this.vendorId = vendorId;
    }

    public Integer getMarketId() { return marketId; }
    public void setMarketId(Integer marketId) { this.marketId = marketId; }
    public Integer getVendorId() { return vendorId; }
    public void setVendorId(Integer vendorId) { this.vendorId = vendorId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketVendorId)) return false;
        MarketVendorId that = (MarketVendorId) o;
        return Objects.equals(marketId, that.marketId) && Objects.equals(vendorId, that.vendorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(marketId, vendorId);
    }
}
