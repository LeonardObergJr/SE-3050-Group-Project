package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "market_vendor")
public class MarketVendor {

    @EmbeddedId
    private MarketVendorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("marketId")
    @JoinColumn(name = "market_id", nullable = false)
    private Market market;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("vendorId")
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(name = "booth_number", length = 30)
    private String boothNumber;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public MarketVendor() {}

    public MarketVendorId getId() { return id; }
    public void setId(MarketVendorId id) { this.id = id; }
    public Market getMarket() { return market; }
    public void setMarket(Market market) { this.market = market; }
    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }
    public String getBoothNumber() { return boothNumber; }
    public void setBoothNumber(String boothNumber) { this.boothNumber = boothNumber; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
