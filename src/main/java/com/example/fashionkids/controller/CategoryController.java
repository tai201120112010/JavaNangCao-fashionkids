package com.example.fashionkids.controller;

import com.example.fashionkids.entity.Category;
import com.example.fashionkids.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryRepository repo;

    @GetMapping
    public List<Category> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public Category add(@RequestBody Category c) {
        return repo.save(c);
    }
}