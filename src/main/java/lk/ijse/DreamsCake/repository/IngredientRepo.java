package lk.ijse.DreamsCake.repository;

import lk.ijse.DreamsCake.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepo extends JpaRepository<Ingredient, Long> {

    @Query("SELECT i FROM Ingredient i WHERE i.quantityInStock <= i.reorderLevel")
    List<Ingredient> findLowStockIngredients();
}