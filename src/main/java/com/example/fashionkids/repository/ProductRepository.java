    package com.example.fashionkids.repository;

    import com.example.fashionkids.entity.Product;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import java.util.List;
    import org.springframework.data.domain.Pageable;
    public interface ProductRepository extends JpaRepository<Product, Long> {

        // Tìm theo tên chứa từ khóa
        List<Product> findByNameContaining(String keyword);

        // Tìm theo tên không phân biệt hoa thường (Dùng cho ShopController)
        List<Product> findByNameContainingIgnoreCase(String keyword);

        // Lọc theo loại (Sửa lỗi 'No property categoryId found')
        List<Product> findByCategoryId(Long categoryId);

        @Query("SELECT p FROM Product p ORDER BY p.id DESC")
        List<Product> findNewest();

        @Query("SELECT p FROM Product p ORDER BY p.price DESC")
        List<Product> findBestSeller();

        // Thêm Pageable để lấy top 10 sản phẩm cho Slider
        @Query("SELECT p FROM Product p ORDER BY p.id DESC")
        List<Product> findNewest(Pageable pageable);

        @Query("SELECT p FROM Product p ORDER BY p.price DESC")
        List<Product> findBestSeller(Pageable pageable);


    }