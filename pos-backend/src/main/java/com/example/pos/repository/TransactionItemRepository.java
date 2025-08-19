package com.example.pos.repository;

import com.example.pos.model.Product;
import com.example.pos.model.Transaction;
import com.example.pos.model.TransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionItemRepository extends JpaRepository<TransactionItem, Long> {
    
    List<TransactionItem> findByTransaction(Transaction transaction);
    
    List<TransactionItem> findByProduct(Product product);
    
    @Query("SELECT ti FROM TransactionItem ti WHERE ti.transaction.status = 'COMPLETED' AND ti.transaction.createdAt BETWEEN :startDate AND :endDate")
    List<TransactionItem> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT ti.product, SUM(ti.quantity) as totalSold FROM TransactionItem ti WHERE ti.transaction.status = 'COMPLETED' GROUP BY ti.product ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProducts();
    
    @Query("SELECT ti.product, SUM(ti.quantity) as totalSold FROM TransactionItem ti WHERE ti.transaction.status = 'COMPLETED' AND ti.transaction.createdAt BETWEEN :startDate AND :endDate GROUP BY ti.product ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProductsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                                    @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(ti.quantity) FROM TransactionItem ti WHERE ti.product = :product AND ti.transaction.status = 'COMPLETED'")
    Long getTotalQuantitySoldForProduct(@Param("product") Product product);
    
    @Query("SELECT SUM(ti.totalPrice) FROM TransactionItem ti WHERE ti.product = :product AND ti.transaction.status = 'COMPLETED'")
    Double getTotalRevenueForProduct(@Param("product") Product product);
}
