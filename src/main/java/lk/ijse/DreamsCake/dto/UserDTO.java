package lk.ijse.DreamsCake.dto;

import lk.ijse.DreamsCake.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private long userId;
    private String userName;
    private String userRoles;
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

}