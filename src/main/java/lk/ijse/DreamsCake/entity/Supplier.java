package lk.ijse.DreamsCake.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "suppliers")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "address")
    private String address;
}