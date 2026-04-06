package com.example.fashionkids.service;

import com.example.fashionkids.entity.Product;
import com.example.fashionkids.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    // Lấy tất cả
    public List<Product> getAll() {
        return repo.findAll();
    }

    // Lấy theo ID
    public Product getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // Thêm / sửa
    public Product save(Product product) {
        return repo.save(product);
    }

    // Xóa
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // 🔍 Tìm kiếm
    public List<Product> search(String name) {
        return repo.findByNameContaining(name);
    }

    // 🆕 Sản phẩm mới
    public List<Product> newest() {
        return repo.findNewest();
    }

    // 🔥 Bán chạy
    public List<Product> bestSeller() {
        return repo.findBestSeller();
    }
}