package com.example.fashionkids.controller;

import com.example.fashionkids.entity.User;
import com.example.fashionkids.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu xác nhận không khớp.");
            redirectAttributes.addFlashAttribute("enteredUsername", username);
            return "redirect:/register";
        }

        if (repo.findByUsername(username) != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tên đăng nhập đã tồn tại.");
            redirectAttributes.addFlashAttribute("enteredUsername", username);
            return "redirect:/register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");

        repo.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký tài khoản thành công. Bạn có thể đăng nhập ngay.");
        return "redirect:/login";
    }
}
