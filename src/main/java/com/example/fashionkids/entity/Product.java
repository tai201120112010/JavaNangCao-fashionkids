package com.example.fashionkids.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "products")
public class Product {

    private static final double MIN_DISCOUNT_PERCENT = 0;
    private static final double MAX_DISCOUNT_PERCENT = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "original_price")
    private Double originalPrice;

    @Column(name = "discount_percent")
    private Double discountPercent;

    private Double price;
    private String image;
    private String description;

    @Column(name = "category_id")
    private Long categoryId;

    @PrePersist
    @PreUpdate
    public void normalizePricing() {
        if (getOriginalPrice() <= 0 && getPrice() > 0) {
            originalPrice = price;
        }

        double normalizedDiscountPercent = getDiscountPercent();
        if (normalizedDiscountPercent < MIN_DISCOUNT_PERCENT) {
            normalizedDiscountPercent = MIN_DISCOUNT_PERCENT;
        }
        if (normalizedDiscountPercent > MAX_DISCOUNT_PERCENT) {
            normalizedDiscountPercent = MAX_DISCOUNT_PERCENT;
        }
        discountPercent = normalizedDiscountPercent;

        price = getOriginalPrice() * (1 - normalizedDiscountPercent / 100.0);
    }

    @Transient
    public double getDiscountAmount() {
        return Math.max(0, getOriginalPrice() - getPrice());
    }

    @Transient
    public boolean hasDiscount() {
        return getDiscountPercent() > 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOriginalPrice() {
        return originalPrice == null ? 0 : originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getDiscountPercent() {
        return discountPercent == null ? 0 : discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public double getPrice() {
        return price == null ? 0 : price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
