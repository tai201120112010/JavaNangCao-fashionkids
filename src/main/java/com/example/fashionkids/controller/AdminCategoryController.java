package com.example.fashionkids.controller;

import com.example.fashionkids.entity.Category;
import com.example.fashionkids.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("category", new Category());
        return "admin/category-list";
    }

    @GetMapping("/save")
    public String saveCategoryByGet(@ModelAttribute Category category) {
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category) {
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.getById(id);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("category", category != null ? category : new Category());
        return "admin/category-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return "redirect:/admin/categories";
    }
}
