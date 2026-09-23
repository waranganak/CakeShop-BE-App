package lk.ijse.DreamsCake.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupplierOrderDTO {
    private Long id;
    private LocalDate orderDate;
    private String status;
    private Long supplierId;
    private List<SupplierOrderDetailDTO> orderDetails;
}