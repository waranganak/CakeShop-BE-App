package lk.ijse.DreamsCake.dto;

import lk.ijse.DreamsCake.enums.PaymentMethod;
import lk.ijse.DreamsCake.enums.PaymentStatus;
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
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Long orderId;
}