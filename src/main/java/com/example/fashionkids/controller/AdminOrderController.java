package com.example.fashionkids.controller;

import com.example.fashionkids.entity.Orders;
import com.example.fashionkids.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderRepository orderRepo;

    @GetMapping
    public String listOrders(Model model) {
        List<Orders> orders = orderRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("orders", orders);
        return "admin/order-list";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long orderId, @RequestParam String status) {
        Orders order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        order.setStatus(status);
        orderRepo.save(order);
        return "redirect:/admin/orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderRepo.deleteById(id);
        return "redirect:/admin/orders";
    }
}
