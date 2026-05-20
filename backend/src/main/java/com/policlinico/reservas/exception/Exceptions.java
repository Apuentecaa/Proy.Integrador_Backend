package com.policlinico.reservas.exception;

import com.policlinico.reservas.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

// ─── Excepciones de negocio ───────────────────────────────

public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) { super(msg); }
}

class BusinessException extends RuntimeException {
    public BusinessException(String msg) { super(msg); }
}

// ─── Manejador global ─────────────────────────────────────

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ApiResponse<Void>> notFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ApiResponse<Void>> business(BusinessException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Map<String, String>>> validation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getAllErrors().forEach(e ->
                errors.put(((FieldError) e).getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Errores de validación", errors));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Void>> general(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error interno: " + ex.getMessage()));
    }
}
