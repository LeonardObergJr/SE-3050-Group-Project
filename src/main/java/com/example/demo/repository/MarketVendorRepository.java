package com.example.demo.repository;

import com.example.demo.entity.MarketVendor;
import com.example.demo.entity.MarketVendorId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MarketVendorRepository extends JpaRepository<MarketVendor, MarketVendorId> {
    List<MarketVendor> findByMarketMarketId(Integer marketId);
    List<MarketVendor> findByVendorVendorId(Integer vendorId);
}
