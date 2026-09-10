package lk.ijse.DreamsCake.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuditLogDTO {
    private Long id;
    private String action;
    private LocalDateTime timestamp;
    private Long userId;
    private String username;
}