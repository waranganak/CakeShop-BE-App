package lk.ijse.DreamsCake.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lk.ijse.DreamsCake.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RiderDTO {

    private Long id;

    @NotBlank(message = "Rider name cannot be empty")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format!")
    private String email;

    private String password;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^07[0-9]{8}$", message = "Invalid phone number format! (Must be like 07XXXXXXXX)")
    private String phone;

    @NotBlank(message = "Vehicle number cannot be empty")
    private String vehicleNumber;

    private String status;

    private RoleType role = RoleType.RIDER;
}