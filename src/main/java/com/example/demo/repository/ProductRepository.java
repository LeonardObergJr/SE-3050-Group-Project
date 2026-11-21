package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("""
        select p from Product p
        join fetch p.vendor v
        join fetch p.category c
        where lower(p.name) like lower(concat('%', :search, '%'))
           or lower(v.businessName) like lower(concat('%', :search, '%'))
           or lower(c.name) like lower(concat('%', :search, '%'))
        order by p.name
        """)
    List<Product> search(@Param("search") String search);

    @Query("""
        select p from Product p
        join fetch p.vendor v
        join fetch p.category c
        order by p.name
        """)
    List<Product> findAllWithDetails();

    @Query("""
        select p from Product p
        join fetch p.vendor v
        join fetch p.category c
        where p.productId = :id
        """)
    Product findDetailedById(@Param("id") Integer id);
}
