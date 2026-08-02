package com.shopsphere.product.exception;

import com.shopsphere.product.product.dto.response.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static String VALIDATION_ERROR = "VALIDATION_ERROR";
    private final static String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    private final static String PRODUCT_NOT_FOUND = "PRODUCT_NOT_FOUND";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> errors = new ArrayList<>();

        ex.getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_GATEWAY.value(),
                        VALIDATION_ERROR,
                        "Request Validation failed",
                        errors,
                        request
                ));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleProductNotFound(
            ProductNotFoundException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        PRODUCT_NOT_FOUND,
                        ex.getMessage(),
                        null,
                        request
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllOther(
            Exception ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred.",
                        null,
                        request
                ));
    }

    private ErrorResponseDto buildErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message,
            List<String> validationErrors,
            HttpServletRequest request
    ) {
        return ErrorResponseDto.builder()
                .timestamp(timestamp)
                .status(status)
                .error(error)
                .message(message)
                .validationErrors(validationErrors)
                .path(request.getRequestURI())
                .build();
    }
}
