package lk.ijse.DreamsCake.dto;

import lk.ijse.DreamsCake.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeliveryDetailsResponseDTO {
    private Long deliveryId;
    private String deliveryStatus;
    private String riderName;
    private Long riderId;

    private Long orderId;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private OrderStatus orderStatus;

    private String paymentMethod;

    private String customerName;
    private String customerPhone;
    private String customerAddress;

    private List<OrderDetailDTO> orderDetails;
}