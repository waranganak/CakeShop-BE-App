package lk.ijse.DreamsCake.service;

import jakarta.validation.Valid;
import lk.ijse.DreamsCake.dto.IngredientDTO;

import java.util.List;

public interface IngredientService {
    void saveIngredient(@Valid IngredientDTO ingredientDTO);

    void updateIngredient(@Valid IngredientDTO ingredientDTO);

    void deleteIngredient(Long id);

    IngredientDTO searchIngredient(Long id);
    List<IngredientDTO> getLowStockIngredients();
    List<IngredientDTO> getAllIngredients();
}
