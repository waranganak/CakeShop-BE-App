package lk.ijse.DreamsCake.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupplierOrderDetailDTO {
    private Double quantity;
    private Long ingredientId;
}