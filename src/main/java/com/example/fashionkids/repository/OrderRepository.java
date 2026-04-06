package com.example.fashionkids.repository;

import com.example.fashionkids.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    // Tính tổng doanh thu từ tất cả đơn hàng thành công
    @Query("SELECT SUM(o.totalPrice) FROM Orders o")
    Double getTotalRevenue();

    // Đếm tổng số đơn hàng
    @Query("SELECT COUNT(o) FROM Orders o")
    Long countAllOrders();

    // Thống kê doanh thu theo tháng (Ví dụ cho biểu đồ)
    @Query(value = "SELECT MONTH(o.order_date) as month, SUM(o.total_price) as amount " +
            "FROM orders o GROUP BY MONTH(o.order_date)", nativeQuery = true)
    List<Object[]> getRevenueByMonth();
}