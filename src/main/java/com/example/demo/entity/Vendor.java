package com.example.demo.entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendor")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_id")
    private Integer vendorId;

    @Column(name = "business_name", nullable = false, length = 150)
    private String businessName;

    @Column(name = "contact_name", length = 150)
    private String contactName;

    @Column(length = 254)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "vendor")
    private List<MarketVendor> marketVendors = new ArrayList<>();

    @OneToMany(mappedBy = "vendor")
    private List<Product> products = new ArrayList<>();

    public Vendor() {}

    public Integer getVendorId() { return vendorId; }
    public void setVendorId(Integer vendorId) { this.vendorId = vendorId; }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<MarketVendor> getMarketVendors() { return marketVendors; }
    public void setMarketVendors(List<MarketVendor> marketVendors) { this.marketVendors = marketVendors; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
