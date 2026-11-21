package com.example.demo.dto;

import java.math.BigDecimal;

public record ProductResponse(
    Integer productId,
    String name,
    String description,
    BigDecimal price,
    Integer stockQuantity,
    String vendorName,
    String categoryName,
    String marketName
) {}
