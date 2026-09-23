package lk.ijse.DreamsCake.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Long userId;
    private Long customerId;


    @NotBlank(message = "Username cannot be blank")
    private String userName;

    private String userRoles;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    public UserDTO(String username, String userRoles, String password) {
        this.userName = username;
        this.userRoles = userRoles;
        this.password = password;
    }

    public UserDTO(long userId, String username, String userRoles) {
        this.userId = userId;
        this.userName = username;
        this.userRoles = userRoles;
    }

    public UserDTO(long userId, Long customerId, String username, String userRoles) {
        this.userId = userId;
        this.customerId = customerId;
        this.userName = username;
        this.userRoles = userRoles;
    }
}