package com.example.fashionkids.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // Ví dụ: GIAM20, HE2024

    private Double percentage; // Phần trăm giảm (ví dụ: 10.0 cho 10%)

    private boolean active = true; // Trạng thái mã còn dùng được không
}