package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
    Integer orderId,
    LocalDateTime orderDate,
    String status,
    BigDecimal totalAmount
) {}
