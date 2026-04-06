package com.example.fashionkids.controller;

import com.example.fashionkids.entity.Discount;
import com.example.fashionkids.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/discounts")
public class AdminDiscountController {

    @Autowired
    private DiscountRepository discountRepo;

    @GetMapping
    public String listDiscounts(Model model) {
        model.addAttribute("discounts", discountRepo.findAll());
        return "admin/discount-list";
    }

    @PostMapping("/add")
    public String addDiscount(@RequestParam String code, @RequestParam Double percentage) {
        Discount d = new Discount();
        d.setCode(code.toUpperCase().trim());
        d.setPercentage(percentage);
        d.setActive(true);
        discountRepo.save(d);
        return "redirect:/admin/discounts";
    }

    @GetMapping("/delete/{id}")
    public String deleteDiscount(@PathVariable Long id) {
        discountRepo.deleteById(id);
        return "redirect:/admin/discounts";
    }
}