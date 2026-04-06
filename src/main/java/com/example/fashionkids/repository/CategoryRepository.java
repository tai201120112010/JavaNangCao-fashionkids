package com.example.fashionkids.repository;

import com.example.fashionkids.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}