package lk.ijse.DreamsCake.exception;

import lk.ijse.DreamsCake.constant.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<CommonResponse> handleServerException(Exception ex, WebRequest webRequest){
        ex.printStackTrace();
        return ResponseEntity.status(500).body(new CommonResponse(500, "UNEXPECTED_ERROR"));
    }

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<CommonResponse> handleCustomException(ApiException ex, WebRequest webRequest){
        ex.printStackTrace();
        return ResponseEntity.status(ex.getStatus()).body(new CommonResponse(ex.getStatus(), ex.getMessage()));
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<CommonResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest webRequest) {
        ex.printStackTrace();
        return ResponseEntity.status(404).body(new CommonResponse(404, ex.getMessage()));
    }

    @ExceptionHandler(value = {DuplicateRecordException.class})
    public ResponseEntity<CommonResponse> handleDuplicateRecordException(DuplicateRecordException ex, WebRequest webRequest) {
        ex.printStackTrace();
        return ResponseEntity.status(409).body(new CommonResponse(409, ex.getMessage()));
    }
    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<CommonResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest webRequest) {
        ex.printStackTrace();
        return ResponseEntity.status(401).body(new CommonResponse(401, ex.getMessage()));
    }

    @ExceptionHandler(value = {AccessDeniedCustomException.class})
    public ResponseEntity<CommonResponse> handleAccessDeniedException(AccessDeniedCustomException ex, WebRequest webRequest) {
        ex.printStackTrace();
        return ResponseEntity.status(403).body(new CommonResponse(403, ex.getMessage()));
    }
}