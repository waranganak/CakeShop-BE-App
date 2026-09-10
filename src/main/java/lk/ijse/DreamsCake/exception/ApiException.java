package lk.ijse.DreamsCake.exception;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerException extends RuntimeException{

    private int status;
    private String message;
}
