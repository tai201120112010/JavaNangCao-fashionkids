package com.example.fashionkids.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<CartItem> items = new ArrayList<>();

    // ✅ FIX CHUẨN – HẾT LỖI
    @Transient
    public long getTotalPrice() {
        return items.stream()
                .mapToLong(i -> (long) (i.getProduct().getPrice() * i.getQuantity()))
                .sum();
    }

    public Long getId() { return id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public List<CartItem> getItems() { return items; }
}
