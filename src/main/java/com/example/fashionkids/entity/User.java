package com.example.fashionkids.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;    // Thêm trường này
    private String fullName; // Thêm trường này (nếu muốn hiện tên thật)
    private String phone;    // Thêm trường này
    private String address;  // Thêm trường này
    private String role;
}