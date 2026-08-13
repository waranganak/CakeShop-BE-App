package lk.ijse.DreamsCake.entity;

import jakarta.persistence.*;
import lk.ijse.DreamsCake.enums.UnitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "ingredients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ingredientName;

    @Enumerated(EnumType.STRING)
    private UnitType unit;

    @OneToOne(mappedBy = "ingredient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Inventory inventory;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SupplierOrderDetail> supplierOrderDetails;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductIngredient> productIngredients;
}
