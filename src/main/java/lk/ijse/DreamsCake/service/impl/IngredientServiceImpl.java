package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.IngredientDTO;
import lk.ijse.DreamsCake.entity.Ingredient;
import lk.ijse.DreamsCake.enums.UnitType;
import lk.ijse.DreamsCake.exception.ApiException;
import lk.ijse.DreamsCake.repository.IngredientRepo;
import lk.ijse.DreamsCake.service.AuditLogService;
import lk.ijse.DreamsCake.service.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepo ingredientRepo;
    private final AuditLogService auditLogService;

    public IngredientServiceImpl(IngredientRepo ingredientRepo, AuditLogService auditLogService) {
        this.ingredientRepo = ingredientRepo;
        this.auditLogService = auditLogService;
    }

    @Override
    public void saveIngredient(IngredientDTO dto) {
        log.info("Saving ingredient: {}", dto.getName());

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new ApiException(400, "Ingredient name cannot be empty");
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName(dto.getName());
        ingredient.setUnit(UnitType.valueOf(dto.getUnit()));
        ingredient.setQuantityInStock(dto.getStockQty() != null ? dto.getStockQty() : 0.0);
        ingredient.setReorderLevel(dto.getReorderLevel() != null ? dto.getReorderLevel() : 0.0);

        ingredientRepo.save(ingredient);

        auditLogService.saveLog("Created ingredient: " + dto.getName(), 1L);
    }

    @Override
    public void updateIngredient(IngredientDTO dto) {
        log.info("Updating ingredient ID: {}", dto.getId());

        Ingredient ingredient = ingredientRepo.findById(dto.getId())
                .orElseThrow(() -> new ApiException(404, "Ingredient not found with ID: " + dto.getId()));

        ingredient.setIngredientName(dto.getName());
        ingredient.setUnit(UnitType.valueOf(dto.getUnit()));
        ingredient.setQuantityInStock(dto.getStockQty() != null ? dto.getStockQty() : 0.0);
        ingredient.setReorderLevel(dto.getReorderLevel() != null ? dto.getReorderLevel() : 0.0);

        ingredientRepo.save(ingredient);

        auditLogService.saveLog("Updated ingredient: " + dto.getName(), 1L);
    }

    @Override
    public void deleteIngredient(Long id) {
        log.info("Deleting ingredient ID: {}", id);

        if (!ingredientRepo.existsById(id)) {
            throw new ApiException(404, "Ingredient not found with ID: " + id);
        }

        ingredientRepo.deleteById(id);
        auditLogService.saveLog("Deleted ingredient ID: " + id, 1L);
    }

    @Override
    public IngredientDTO searchIngredient(Long id) {
        log.info("Searching ingredient by ID: {}", id);

        Ingredient ingredient = ingredientRepo.findById(id)
                .orElseThrow(() -> new ApiException(404, "Ingredient not found with ID: " + id));

        return new IngredientDTO(
                ingredient.getId(),
                ingredient.getIngredientName(),
                ingredient.getUnit() != null ? ingredient.getUnit().name() : "",
                ingredient.getQuantityInStock(),
                ingredient.getReorderLevel()
        );
    }

    @Override
    public List<IngredientDTO> getAllIngredients() {
        log.info("Fetching all ingredients from database");

        List<Ingredient> ingredients = ingredientRepo.findAll();
        return ingredients.stream().map(ing -> new IngredientDTO(
                ing.getId(),
                ing.getIngredientName(),
                ing.getUnit() != null ? ing.getUnit().name() : "",
                ing.getQuantityInStock() != null ? ing.getQuantityInStock() : 0.0,
                ing.getReorderLevel() != null ? ing.getReorderLevel() : 0.0
        )).collect(Collectors.toList());
    }
}