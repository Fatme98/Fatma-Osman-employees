package com.example.employee.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.EXPECTATION_FAILED, reason = "DateFrom should be before DateTo in the time")
public class NegativeDaysBetweenDates extends RuntimeException{
}
