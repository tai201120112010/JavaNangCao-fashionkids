package com.example.fashionkids.controller;

import com.example.fashionkids.entity.Cart;
import com.example.fashionkids.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // ✅ HIỂN THỊ GIỎ HÀNG
    @GetMapping("")
    public String viewCart(Model model) {
        Cart cart = cartService.getCart();

        model.addAttribute("cart", cart);
        model.addAttribute("items", cart.getItems());
        model.addAttribute("total", cart.getTotalPrice());

        return "cart";
    }

    // ✅ THÊM SẢN PHẨM
    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id) {
        cartService.addToCart(id);
        return "redirect:/cart";
    }

    // ✅ UPDATE SỐ LƯỢNG (AJAX)
    @PostMapping("/update")
    @ResponseBody
    public String updateCart(@RequestParam Long itemId,
                             @RequestParam int quantity) {
        cartService.updateQuantity(itemId, quantity);
        return "ok";
    }

    // ✅ XÓA ITEM (AJAX – POST, KHÔNG DÙNG GET)
    @PostMapping("/remove/{id}")
    @ResponseBody
    public String removeItem(@PathVariable Long id) {
        cartService.removeItem(id);
        return "ok";
    }

    // ✅ XÓA TOÀN BỘ GIỎ
    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}