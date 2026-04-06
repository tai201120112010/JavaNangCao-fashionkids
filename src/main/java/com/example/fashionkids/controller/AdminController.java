package com.example.fashionkids.controller;

import com.example.fashionkids.repository.OrderRepository;
import com.example.fashionkids.repository.ProductRepository;
import com.example.fashionkids.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        // 1. Lấy các con số tổng quát
        Double totalRevenue = orderRepo.getTotalRevenue();
        model.addAttribute("totalRevenue", totalRevenue != null ? totalRevenue : 0);
        model.addAttribute("totalOrders", orderRepo.countAllOrders());
        model.addAttribute("totalProducts", productRepo.count());
        model.addAttribute("totalUsers", userRepo.count());

        // 2. Dữ liệu cho biểu đồ (giả lập hoặc từ DB)
        // Ở đây mình truyền dữ liệu mẫu để bạn dễ hình dung
        model.addAttribute("chartData", new double[]{1200000, 1900000, 3000000, 5000000, 2400000, 3500000});

        return "admin/dashboard";
    }
}