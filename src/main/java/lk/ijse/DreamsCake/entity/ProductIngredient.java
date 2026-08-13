package lk.ijse.DreamsCake.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_ingredients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double requiredQuantity;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Ingredient ingredient;
}