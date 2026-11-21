package com.example.demo.service;

import com.example.demo.dto.MarketResponse;
import com.example.demo.dto.VendorResponse;
import com.example.demo.entity.Market;
import com.example.demo.entity.MarketVendor;
import com.example.demo.repository.MarketRepository;
import com.example.demo.repository.MarketVendorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MarketService {
    private final MarketRepository marketRepository;
    private final MarketVendorRepository marketVendorRepository;

    public MarketService(MarketRepository marketRepository, MarketVendorRepository marketVendorRepository) {
        this.marketRepository = marketRepository;
        this.marketVendorRepository = marketVendorRepository;
    }

    public List<MarketResponse> findAll() {
        return marketRepository.findAll().stream().map(this::toResponse).toList();
    }

    public MarketResponse findById(Integer id) {
        return toResponse(marketRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Market not found")));
    }

    public List<VendorResponse> findVendors(Integer marketId) {
        if (!marketRepository.existsById(marketId)) {
            throw new RuntimeException("Market not found");
        }
        return marketVendorRepository.findByMarketMarketId(marketId).stream()
            .map(this::vendorResponse).toList();
    }

    private MarketResponse toResponse(Market m) {
        return new MarketResponse(m.getMarketId(), m.getName(), m.getAddress(),
            m.getCity(), m.getOperatingDays(), m.getDescription());
    }

    private VendorResponse vendorResponse(MarketVendor mv) {
        var v = mv.getVendor();
        return new VendorResponse(v.getVendorId(), v.getBusinessName(), v.getContactName(),
            v.getEmail(), v.getPhone(), v.getDescription(), mv.getBoothNumber());
    }
}
