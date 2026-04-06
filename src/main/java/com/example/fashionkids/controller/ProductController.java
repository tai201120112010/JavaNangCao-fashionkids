package com.example.fashionkids.controller;

import com.example.fashionkids.entity.Product;
import com.example.fashionkids.repository.OrderDetailRepository;
import com.example.fashionkids.repository.ProductRepository;
import com.example.fashionkids.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private static final double MIN_DISCOUNT_PERCENT = 0;
    private static final double MAX_DISCOUNT_PERCENT = 100;

    @Autowired
    private ProductRepository repo;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", repo.findAll());
        return "admin/products";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "admin/add-product";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Product product) {
        normalizeProductPricing(product);
        repo.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = repo.findById(id).orElseThrow();
        hydrateLegacyPricing(product);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAll());
        return "admin/edit-product";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Product product) {
        normalizeProductPricing(product);
        repo.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Product product = repo.findById(id).orElseThrow();

        if (orderDetailRepository.existsByProductId(id)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Không thể xóa sản phẩm \"" + product.getName() + "\" vì sản phẩm này đã tồn tại trong đơn hàng."
            );
            return "redirect:/admin/products";
        }

        repo.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công.");
        return "redirect:/admin/products";
    }

    @GetMapping("/product/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product p = repo.findById(id).orElseThrow();
        model.addAttribute("product", p);
        return "product-detail";
    }

    private void normalizeProductPricing(Product product) {
        if (product.getOriginalPrice() <= 0 && product.getPrice() > 0) {
            product.setOriginalPrice(product.getPrice());
        }

        double discountPercent = product.getDiscountPercent();
        if (discountPercent < MIN_DISCOUNT_PERCENT) {
            discountPercent = MIN_DISCOUNT_PERCENT;
        }
        if (discountPercent > MAX_DISCOUNT_PERCENT) {
            discountPercent = MAX_DISCOUNT_PERCENT;
        }

        product.setDiscountPercent(discountPercent);
        product.setPrice(product.getOriginalPrice() * (1 - discountPercent / 100.0));
    }

    private void hydrateLegacyPricing(Product product) {
        if (product.getOriginalPrice() <= 0 && product.getPrice() > 0) {
            product.setOriginalPrice(product.getPrice());
        }
        if (product.getDiscountPercent() < MIN_DISCOUNT_PERCENT) {
            product.setDiscountPercent(MIN_DISCOUNT_PERCENT);
        }
    }
}
