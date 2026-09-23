package lk.ijse.DreamsCake.repository;

import lk.ijse.DreamsCake.dto.ProductDTO;
import lk.ijse.DreamsCake.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {


        @Query("SELECT new lk.ijse.DreamsCake.dto.ProductDTO(p.id, p.productName, p.description, p.price, p.qty, p.imageUrl, p.category.id) FROM Product p")
        List<ProductDTO> getAllProducts();

        Product findTopByOrderByIdDesc();

        Optional<Product> findByProductName(String productName);
    }