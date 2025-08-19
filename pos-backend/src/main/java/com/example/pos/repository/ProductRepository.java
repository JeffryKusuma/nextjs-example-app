package com.example.pos.repository;

import com.example.pos.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findBySku(String sku);
    
    Optional<Product> findByBarcode(String barcode);
    
    List<Product> findByActiveTrue();
    
    List<Product> findByCategory(Product.Category category);
    
    List<Product> findByActiveTrueAndCategory(Product.Category category);
    
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.name LIKE %:name%")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);
    
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.quantity <= p.minStockLevel")
    List<Product> findLowStockProducts();
    
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.quantity = 0")
    List<Product> findOutOfStockProducts();
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.active = true")
    long countActiveProducts();
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.active = true AND p.quantity <= p.minStockLevel")
    long countLowStockProducts();
    
    @Query("SELECT SUM(p.quantity * p.price) FROM Product p WHERE p.active = true")
    Double getTotalInventoryValue();
    
    boolean existsBySku(String sku);
    
    boolean existsByBarcode(String barcode);
}
