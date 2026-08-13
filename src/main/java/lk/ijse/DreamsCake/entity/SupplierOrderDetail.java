package lk.ijse.DreamsCake.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supplier_order_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double quantity;
    private Double unitPrice;

    @ManyToOne
    private SupplierOrder supplierOrder;

    @ManyToOne
    private Ingredient ingredient;
}