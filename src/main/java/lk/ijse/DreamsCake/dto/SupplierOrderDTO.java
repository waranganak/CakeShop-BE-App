package lk.ijse.DreamsCake.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupplierOrderDTO {
    private Long id;
    private LocalDate orderDate;
    private Double totalCost;
    private Long supplierId;
}