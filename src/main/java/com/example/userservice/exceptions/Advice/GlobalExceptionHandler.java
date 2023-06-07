package com.example.userservice.exceptions.Advice;

import com.example.userservice.exceptions.ApiResponse;
import com.example.userservice.exceptions.Custom.ResourceNotFoundException;
import com.example.userservice.exceptions.Custom.UserAlreadyExit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundException(ResourceNotFoundException ex) {
        //String message = ex.getMessage();
        ApiResponse response = new ApiResponse(ex.getMessage(), false);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(new ApiResponse("Id should be an integer",false),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExit.class)
    public ResponseEntity<ApiResponse> userNameFoundException(UserAlreadyExit ex) {
        return new ResponseEntity<>(new ApiResponse(ex.getMessage(),false),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ApiResponse("First Name or Last Name should not null",false),HttpStatus.BAD_REQUEST);
    }

}
