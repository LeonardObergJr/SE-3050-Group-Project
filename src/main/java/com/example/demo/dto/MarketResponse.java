package com.example.demo.dto;

public record MarketResponse(
    Integer marketId,
    String name,
    String address,
    String city,
    String operatingDays,
    String description
) {}
