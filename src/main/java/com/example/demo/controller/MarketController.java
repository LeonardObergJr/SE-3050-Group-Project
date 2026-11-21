package com.example.demo.controller;

import com.example.demo.dto.MarketResponse;
import com.example.demo.dto.VendorResponse;
import com.example.demo.service.MarketService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/markets")
public class MarketController {
    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping
    public List<MarketResponse> getMarkets() {
        return marketService.findAll();
    }

    @GetMapping("/{id}")
    public MarketResponse getMarket(@PathVariable Integer id) {
        return marketService.findById(id);
    }

    @GetMapping("/{id}/vendors")
    public List<VendorResponse> getVendors(@PathVariable Integer id) {
        return marketService.findVendors(id);
    }
}
