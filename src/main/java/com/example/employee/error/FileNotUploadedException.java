package com.example.employee.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "File not uploaded successfully! Try Again!")
public class FileNotUploadedException extends RuntimeException{

}
