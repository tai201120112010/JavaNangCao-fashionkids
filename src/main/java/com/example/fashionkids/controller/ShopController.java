package com.example.fashionkids.controller;

import com.example.fashionkids.entity.Product;
import com.example.fashionkids.repository.OrderDetailRepository;
import com.example.fashionkids.repository.ProductRepository;
import com.example.fashionkids.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class ShopController {

    private static final String BEST_SELLER_TAB = "best-seller";
    private static final String NEWEST_TAB = "newest";
    private static final String DISCOUNT_TAB = "discount";

    @Autowired
    private ProductRepository repo;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/shop")
    public String shop(@RequestParam(name = "keyword", required = false) String keyword,
                       @RequestParam(name = "category", required = false) Long categoryId,
                       @RequestParam(name = "tab", required = false, defaultValue = BEST_SELLER_TAB) String tab,
                       Model model) {
        List<Product> baseProducts;

        if (keyword != null && !keyword.isEmpty()) {
            baseProducts = repo.findByNameContainingIgnoreCase(keyword);
            model.addAttribute("keyword", keyword);
        } else if (categoryId != null) {
            baseProducts = repo.findByCategoryId(categoryId);
            model.addAttribute("selectedCat", categoryId);
        } else {
            baseProducts = repo.findAll();
        }

        model.addAttribute("products", applyTabFilter(baseProducts, tab));
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("activeTab", normalizeTab(tab));
        return "shop";
    }

    @GetMapping("/product/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("product", repo.findById(id).orElseThrow());
        return "product-detail";
    }

    private List<Product> applyTabFilter(List<Product> baseProducts, String tab) {
        String normalizedTab = normalizeTab(tab);

        return switch (normalizedTab) {
            case NEWEST_TAB -> baseProducts.stream()
                    .sorted(Comparator.comparing(Product::getId, Comparator.nullsLast(Long::compareTo)).reversed())
                    .toList();
            case DISCOUNT_TAB -> baseProducts.stream()
                    .filter(product -> product.getDiscountPercent() > 0)
                    .sorted(Comparator.comparing(Product::getId, Comparator.nullsLast(Long::compareTo)).reversed())
                    .toList();
            default -> filterBestSellingProducts(baseProducts);
        };
    }

    private List<Product> filterBestSellingProducts(List<Product> baseProducts) {
        List<Long> bestSellingIds = orderDetailRepository.findBestSellingProductIds();
        if (bestSellingIds.isEmpty()) {
            return List.of();
        }

        Map<Long, Product> productMap = baseProducts.stream()
                .filter(product -> product.getId() != null)
                .collect(Collectors.toMap(Product::getId, Function.identity(), (first, second) -> first, LinkedHashMap::new));

        return bestSellingIds.stream()
                .map(productMap::get)
                .filter(product -> product != null)
                .toList();
    }

    private String normalizeTab(String tab) {
        if (NEWEST_TAB.equals(tab)) {
            return NEWEST_TAB;
        }
        if (DISCOUNT_TAB.equals(tab)) {
            return DISCOUNT_TAB;
        }
        return BEST_SELLER_TAB;
    }
}
