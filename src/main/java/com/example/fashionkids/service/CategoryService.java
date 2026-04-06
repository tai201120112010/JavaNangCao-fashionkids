package com.example.fashionkids.service;

import com.example.fashionkids.entity.Category;
import com.example.fashionkids.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    // Lấy tất cả danh mục
    public List<Category> getAll() {
        return repo.findAll();
    }

    // Lấy theo ID
    public Category getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // Thêm / sửa
    public Category save(Category category) {
        return repo.save(category);
    }

    // Xóa
    public void delete(Long id) {
        repo.deleteById(id);
    }
}