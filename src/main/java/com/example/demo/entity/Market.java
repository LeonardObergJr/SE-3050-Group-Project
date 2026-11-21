package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "market")
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id")
    private Integer marketId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(name = "operating_days", length = 255)
    private String operatingDays;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "market")
    private List<MarketVendor> marketVendors = new ArrayList<>();

    public Market() {}

    public Integer getMarketId() { return marketId; }
    public void setMarketId(Integer marketId) { this.marketId = marketId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getOperatingDays() { return operatingDays; }
    public void setOperatingDays(String operatingDays) { this.operatingDays = operatingDays; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<MarketVendor> getMarketVendors() { return marketVendors; }
    public void setMarketVendors(List<MarketVendor> marketVendors) { this.marketVendors = marketVendors; }
}
