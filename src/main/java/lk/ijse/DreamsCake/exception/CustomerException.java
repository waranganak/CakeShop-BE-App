package com.example.spring_security_test.exception;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerException extends RuntimeException{

    private int status;
    private String message;
}
