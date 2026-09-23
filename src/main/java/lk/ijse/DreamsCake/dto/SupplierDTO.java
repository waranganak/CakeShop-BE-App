package lk.ijse.DreamsCake.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupplierDTO {
    private Long id;

    @NotBlank(message = "Supplier name cannot be blank")
    private String name;

    @NotBlank(message = "Contact number cannot be blank")
    @Pattern(regexp = "^0\\d{9}$", message = "Invalid contact number format (Ex: 0712345678)")
    private String contact;

    @NotBlank(message = "Address cannot be blank")
    private String address;
}