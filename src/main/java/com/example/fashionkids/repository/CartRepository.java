package com.example.fashionkids.repository;
import com.example.fashionkids.entity.User;
import com.example.fashionkids.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}