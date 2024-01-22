package com.example.employee.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "There should be a pair of employees")
public class PairNotFoundException extends RuntimeException{
}
