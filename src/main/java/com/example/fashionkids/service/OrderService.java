package com.example.fashionkids.service;

import com.example.fashionkids.entity.Cart;
import com.example.fashionkids.entity.CartItem;
import com.example.fashionkids.entity.OrderDetail;
import com.example.fashionkids.entity.Orders;
import com.example.fashionkids.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private CartService cartService;

    @Transactional
    public void placeOrder(String name, String phone, String address, Double discountPercent) {
        Cart cart = cartService.getCart();
        List<CartItem> cartItems = new ArrayList<>(cart.getItems());
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Giỏ hàng trống.");
        }

        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        double appliedDiscount = discountPercent != null ? discountPercent : 0;
        double finalTotal = total * (1 - appliedDiscount / 100);

        Orders order = new Orders();
        order.setCustomerName(name);
        order.setPhone(phone);
        order.setAddress(address);
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalPrice(finalTotal);

        List<OrderDetail> orderDetails = cartItems.stream().map(item -> {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getProduct().getPrice());
            return detail;
        }).collect(Collectors.toList());

        order.setOrderDetails(new ArrayList<>(orderDetails));
        orderRepo.save(order);
        cartService.clear();
    }
}
