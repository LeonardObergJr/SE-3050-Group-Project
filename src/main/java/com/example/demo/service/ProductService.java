package com.example.demo.service;

import com.example.demo.dto.ProductResponse;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findAll(String search) {
        List<Product> products = search == null || search.isBlank()
            ? productRepository.findAllWithDetails()
            : productRepository.search(search);
        return products.stream().map(this::toResponse).toList();
    }

    public ProductResponse findById(Integer id) {
        Product product = productRepository.findDetailedById(id);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        return toResponse(product);
    }

    private ProductResponse toResponse(Product p) {
        String marketName = p.getVendor().getMarketVendors().stream()
            .map(mv -> mv.getMarket().getName())
            .findFirst().orElse(null);

        return new ProductResponse(
            p.getProductId(), p.getName(), p.getDescription(), p.getPrice(),
            p.getStockQuantity(), p.getVendor().getBusinessName(),
            p.getCategory().getName(), marketName
        );
    }
}
