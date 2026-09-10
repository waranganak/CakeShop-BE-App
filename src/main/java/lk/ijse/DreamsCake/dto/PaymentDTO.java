package lk.ijse.DreamsCake.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {
    private Long id;
    private LocalDate paymentDate;
    private Double amount;
    private String paymentMethod;
    private Long orderId;
}