package com.example.fashionkids.controller;

import com.example.fashionkids.entity.Orders;
import com.example.fashionkids.repository.DiscountRepository;
import com.example.fashionkids.service.CartService;
import com.example.fashionkids.service.OrderService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DiscountRepository discountRepo;

    // 1. Hiển thị trang thanh toán
    @GetMapping
    public String showCheckout(Model model, HttpSession session) {
        if (cartService.getItems().isEmpty()) {
            return "redirect:/cart"; // Giỏ hàng trống thì không cho thanh toán
        }

        double totalOriginal = cartService.getAmount();
        Double discountPercent = (Double) session.getAttribute("discountPercent");
        if (discountPercent == null) discountPercent = 0.0;

        double discountAmount = totalOriginal * (discountPercent / 100);
        double finalTotal = totalOriginal - discountAmount;

        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("totalOriginal", totalOriginal);
        model.addAttribute("discountAmount", discountAmount);
        model.addAttribute("finalTotal", finalTotal);

        return "checkout";
    }

    // 2. Xử lý áp dụng mã giảm giá
    @PostMapping("/apply-discount")
    public String applyDiscount(@RequestParam String couponCode, HttpSession session, RedirectAttributes ra) {
        var discountOpt = discountRepo.findByCodeAndActiveTrue(couponCode.toUpperCase().trim());

        if (discountOpt.isPresent()) {
            double percent = discountOpt.get().getPercentage();
            session.setAttribute("discountPercent", percent);
            session.setAttribute("appliedCoupon", couponCode.toUpperCase());
            ra.addFlashAttribute("successMsg", "Áp dụng mã thành công! Giảm " + percent + "%");
        } else {
            session.removeAttribute("discountPercent");
            session.removeAttribute("appliedCoupon");
            ra.addFlashAttribute("errorMsg", "Mã giảm giá không hợp lệ hoặc đã hết hạn.");
        }
        return "redirect:/checkout";
    }

    // 3. Xác nhận đặt hàng
    @PostMapping("/confirm")
    public String confirmOrder(@RequestParam String name,
                               @RequestParam String phone,
                               @RequestParam String address,
                               HttpSession session,
                               RedirectAttributes ra) {
        try {
            Double discountPercent = (Double) session.getAttribute("discountPercent");
            if (discountPercent == null) discountPercent = 0.0;

            // Lưu đơn hàng vào database thông qua Service
            orderService.placeOrder(name, phone, address, discountPercent);

            // Xóa thông tin giảm giá và giỏ hàng sau khi thành công
            session.removeAttribute("discountPercent");
            session.removeAttribute("appliedCoupon");

            return "redirect:/checkout/success";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Có lỗi xảy ra khi đặt hàng: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/success")
    public String orderSuccess() {
        return "order-success";
    }
}