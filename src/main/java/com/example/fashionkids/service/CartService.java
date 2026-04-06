package com.example.fashionkids.service;

import com.example.fashionkids.entity.Cart;
import com.example.fashionkids.entity.CartItem;
import com.example.fashionkids.entity.Product;
import com.example.fashionkids.entity.User;
import com.example.fashionkids.repository.CartItemRepository;
import com.example.fashionkids.repository.CartRepository;
import com.example.fashionkids.repository.ProductRepository;
import com.example.fashionkids.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart getCart() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Khong tim thay tai khoan dang dang nhap.");
        }

        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    public void addToCart(Long productId) {
        Cart cart = getCart();

        Optional<CartItem> optionalItem =
                cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            item.setQuantity(item.getQuantity() + 1);
            if (item.getUserId() == null && cart.getUser() != null) {
                item.setUserId(cart.getUser().getId());
            }
            cartItemRepository.save(item);
            return;
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(1);
        if (cart.getUser() != null) {
            item.setUserId(cart.getUser().getId());
        }

        cartItemRepository.save(item);
        cart.getItems().add(item);
    }

    public void updateQuantity(Long itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId).orElse(null);

        if (item != null) {
            if (quantity <= 0) {
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        }
    }

    public void removeItem(Long itemId) {
        Cart cart = getCart();
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cartRepository.save(cart);
    }

    public void clearCart() {
        Cart cart = getCart();
        cartRepository.delete(cart);
    }

    public List<CartItem> getItems() {
        return getCart().getItems();
    }

    public double getAmount() {
        return getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public void clear() {
        Cart cart = getCart();
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
