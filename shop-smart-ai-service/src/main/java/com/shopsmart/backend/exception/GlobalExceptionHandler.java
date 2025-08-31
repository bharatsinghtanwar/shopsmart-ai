package com.shopsmart.backend.exception;

import com.shopsmart.backend.dto.ApiResponse;
import com.shopsmart.backend.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        return new ResponseEntity<>(ResponseUtil.error(errorMessage), HttpStatus.BAD_REQUEST);
    }

    // Handle all other exceptions (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(ResponseUtil.error(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Custom handler for runtime exceptions (optional, if you want finer control)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ResponseUtil.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
