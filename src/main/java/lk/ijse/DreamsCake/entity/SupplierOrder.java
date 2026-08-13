package lk.ijse.DreamsCake.entity;


import jakarta.persistence.*;
import lk.ijse.DreamsCake.enums.SupplierOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "supplier_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    private SupplierOrderStatus status;

    @ManyToOne
    private Supplier supplier;

    @OneToMany(mappedBy = "supplierOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SupplierOrderDetail> supplierOrderDetails;
}