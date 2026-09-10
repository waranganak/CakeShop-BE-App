package lk.ijse.DreamsCake.repository;

import lk.ijse.DreamsCake.entity.Product;
import lk.ijse.DreamsCake.entity.ProductIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductIngredientRepo extends JpaRepository<ProductIngredient, Long> {

    List<ProductIngredient> findByProduct(Product product);

    void deleteByProduct(Product product);
}