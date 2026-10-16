package com.ecommerce.catalogo.infraestructure.entry_points;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleIllegalArgumentException_WhenNotFound_Returns404() {
        IllegalArgumentException ex = new IllegalArgumentException("Producto no encontrado con ID: 123");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("Not Found", response.getBody().get("error"));
        assertEquals("Producto no encontrado con ID: 123", response.getBody().get("message"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void handleIllegalArgumentException_WhenNotExists_Returns404() {
        IllegalArgumentException ex = new IllegalArgumentException("El producto no existe");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("Not Found", response.getBody().get("error"));
        assertEquals("El producto no existe", response.getBody().get("message"));
    }

    @Test
    void handleIllegalArgumentException_WhenOther_Returns400() {
        IllegalArgumentException ex = new IllegalArgumentException("La cantidad debe ser mayor a 0");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("La cantidad debe ser mayor a 0", response.getBody().get("message"));
    }

    @Test
    void handleIllegalStateException_Returns400() {
        IllegalStateException ex = new IllegalStateException("Stock insuficiente. Disponible: 2");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIllegalStateException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("Stock insuficiente. Disponible: 2", response.getBody().get("message"));
    }

    @Test
    void handleRetryableException_Returns503() {
        Request request = Request.create(Request.HttpMethod.GET, "/test", Collections.emptyMap(), null, new RequestTemplate());
        RetryableException ex = new RetryableException(503, "Connection refused", Request.HttpMethod.GET, new Date(), request);

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleRetryableException(ex);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(503, response.getBody().get("status"));
        assertEquals("Service Unavailable", response.getBody().get("error"));
        assertEquals("El servicio de autenticación/usuarios no se encuentra disponible temporalmente", response.getBody().get("message"));
    }

    @Test
    void handleFeignException_WithValidHttpStatusAndBody_PropagatesCodeAndBody() {
        Request request = Request.create(Request.HttpMethod.PATCH, "/debitar", Collections.emptyMap(), null, new RequestTemplate());
        byte[] body = "{\"error\": \"Saldo insuficiente\"}".getBytes(StandardCharsets.UTF_8);
        FeignException.BadRequest ex = new FeignException.BadRequest(
                "Bad Request",
                request,
                body,
                Collections.emptyMap()
        );

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleFeignException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("{\"error\": \"Saldo insuficiente\"}", response.getBody().get("message"));
    }

    @Test
    void handleFeignException_WithInvalidHttpStatus_Returns502() {
        Request request = Request.create(Request.HttpMethod.GET, "/test", Collections.emptyMap(), null, new RequestTemplate());
        Response responseFeign = Response.builder()
                .status(999)
                .reason("Unknown Status")
                .request(request)
                .headers(Collections.emptyMap())
                .build();
        FeignException ex = FeignException.errorStatus("testMethod", responseFeign);

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleFeignException(ex);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(502, response.getBody().get("status"));
        assertEquals("Bad Gateway", response.getBody().get("error"));
        assertNotNull(response.getBody().get("message"));
    }

    @Test
    void handleHttpMessageNotReadableException_Returns400() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("JSON parse error", (HttpInputMessage) null);
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleHttpMessageNotReadableException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("Cuerpo de la petición inválido o malformado", response.getBody().get("message"));
    }

    @Test
    void handleGeneralException_Returns500() {
        Exception ex = new NullPointerException("Something unexpected");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().get("status"));
        assertEquals("Internal Server Error", response.getBody().get("error"));
        assertEquals("Error interno en el servidor", response.getBody().get("message"));
    }
}
