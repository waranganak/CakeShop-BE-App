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
public class CustomerOrderDTO {
    private Long id;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private OrderStatus status;
    private Long customerId;
    private List<OrderDetailDTO> orderDetails;

    public CustomerOrderDTO(Long id, LocalDateTime orderDate, Double totalAmount, OrderStatus status, Long customerId) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.customerId = customerId;
    }


}