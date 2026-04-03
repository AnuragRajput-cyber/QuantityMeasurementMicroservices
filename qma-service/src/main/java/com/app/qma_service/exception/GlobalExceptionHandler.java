package com.app.qma_service.exception;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.qma_service.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUnitException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUnit(InvalidUnitException exception) {
        return build(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedOperation(UnsupportedOperationException exception) {
        return build(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
        return build(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return build(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception exception) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                message
        ));
    }
}
