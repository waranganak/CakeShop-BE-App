package lk.ijse.DreamsCake.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiException extends RuntimeException {
    private int status = 400;
    private String message;

    public ApiException(String message) {
        super(message);
        this.message = message;
    }
}