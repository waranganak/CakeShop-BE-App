package lk.ijse.DreamsCake.entity;


import jakarta.persistence.*;
import lk.ijse.DreamsCake.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deliveries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus deliveryStatus;

    @OneToOne
    private CustomerOrder customerOrder;

    @ManyToOne
    private User rider;
}