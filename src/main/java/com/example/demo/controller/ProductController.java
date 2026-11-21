package com.example.demo.controller;

import com.example.demo.dto.ProductResponse;
import com.example.demo.service.ProductService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> getProducts(@RequestParam(required = false) String search) {
        return productService.findAll(search);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Integer id) {
        return productService.findById(id);
    }
}
