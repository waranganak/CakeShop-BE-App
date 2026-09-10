package lk.ijse.DreamsCake.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IngredientDTO {
    private Long id;
    private String name;
    private String unit;
    private Double stockQty;
    private Double reorderLevel;
}