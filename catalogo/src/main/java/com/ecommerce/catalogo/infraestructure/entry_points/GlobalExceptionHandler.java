package com.ecommerce.catalogo.infraestructure.entry_points;

import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", message);
        return ResponseEntity.status(status).body(errorDetails);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "";
        String lowerCaseMessage = message.toLowerCase();
        HttpStatus status = (lowerCaseMessage.contains("no encontrado") || lowerCaseMessage.contains("no existe"))
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;
        return buildResponse(status, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalStateException(IllegalStateException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "";
        String lowerCaseMessage = message.toLowerCase();
        HttpStatus status = (lowerCaseMessage.contains("ya tiene un carrito") || lowerCaseMessage.contains("conflicto"))
                ? HttpStatus.CONFLICT
                : HttpStatus.BAD_REQUEST;
        return buildResponse(status, ex.getMessage());
    }

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<Map<String, Object>> handleRetryableException(RetryableException ex) {
        log.error("Error de reintento/comunicación en cliente Feign", ex);
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, "El servicio de autenticación/usuarios no se encuentra disponible temporalmente");
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException ex) {
        log.error("Error devuelto por servicio externo (Feign): status={}", ex.status(), ex);
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) {
            status = HttpStatus.BAD_GATEWAY;
        }

        String message = ex.contentUTF8();
        if (message == null || message.trim().isEmpty()) {
            message = ex.getMessage() != null ? ex.getMessage() : "Error en la comunicación con el servicio externo";
        }

        return buildResponse(status, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("Cuerpo de la petición inválido o malformado: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Cuerpo de la petición inválido o malformado");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validación fallida");
        return buildResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFoundException(NoResourceFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Ruta no encontrada: " + ex.getResourcePath());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        log.error("Error interno no controlado en el servidor", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno en el servidor");
    }
}