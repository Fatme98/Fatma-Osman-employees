package com.example.employee.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "Input data not found")
public class InputDataNotPresent extends RuntimeException{
    public InputDataNotPresent(String message) {
        super(message);
    }
}
