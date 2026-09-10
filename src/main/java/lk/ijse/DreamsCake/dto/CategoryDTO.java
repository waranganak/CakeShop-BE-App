package lk.ijse.DreamsCake.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "Category name cannot be blank")
    private String categoryName;

    @NotBlank(message = "Description cannot be blank")
    private String description;
}