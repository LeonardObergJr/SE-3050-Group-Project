package com.example.demo.service;

import com.example.demo.dto.OrderItemRequest;
import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class OrderService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        Customer customer = customerRepository.findByEmail(request.customer().email())
            .orElseGet(Customer::new);

        customer.setFirstName(request.customer().firstName());
        customer.setLastName(request.customer().lastName());
        customer.setEmail(request.customer().email());
        customer.setPhone(request.customer().phone());
        customer = customerRepository.save(customer);

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus("Pending");

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.productId()));

            if (product.getStockQuantity() < itemRequest.quantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getName());
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(product.getPrice());
            item.setId(new OrderItemId(null, product.getProductId()));

            order.getOrderItems().add(item);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            product.setStockQuantity(product.getStockQuantity() - itemRequest.quantity());
        }

        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);

        for (OrderItem item : saved.getOrderItems()) {
            item.setId(new OrderItemId(saved.getOrderId(), item.getProduct().getProductId()));
        }

        return new OrderResponse(saved.getOrderId(), saved.getOrderDate(),
            saved.getStatus(), saved.getTotalAmount());
    }
}
