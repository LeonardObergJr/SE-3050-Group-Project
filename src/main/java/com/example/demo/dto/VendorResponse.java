package com.example.demo.dto;

public record VendorResponse(
    Integer vendorId,
    String businessName,
    String contactName,
    String email,
    String phone,
    String description,
    String boothNumber
) {}
