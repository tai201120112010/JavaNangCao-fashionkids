package com.example.fashionkids.repository;

import com.example.fashionkids.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("""
            SELECT od.product.id
            FROM OrderDetail od
            GROUP BY od.product.id
            ORDER BY SUM(od.quantity) DESC, MAX(od.id) DESC
            """)
    List<Long> findBestSellingProductIds();

    boolean existsByProductId(Long productId);
}
