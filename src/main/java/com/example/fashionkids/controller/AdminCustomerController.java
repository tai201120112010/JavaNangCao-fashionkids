package com.example.fashionkids.controller;

import com.example.fashionkids.entity.User;
import com.example.fashionkids.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/customers")
public class AdminCustomerController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping
    public String listCustomers(Model model) {
        // Lấy tất cả user có role là 'USER' (không lấy ADMIN)
        List<User> customers = userRepo.findAll();
        model.addAttribute("customers", customers);
        return "admin/customer-list";
    }
}