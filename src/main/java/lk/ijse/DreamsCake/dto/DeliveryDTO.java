package lk.ijse.DreamsCake.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeliveryDTO {
    private Long id;
    private String deliveryStatus;
    private String address;
    private String riderName;
    private Long orderId;
}