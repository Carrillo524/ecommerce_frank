package com.ecommerce.auth.infraestructure.entry_points.Dto;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

class RequestSaldoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deserializarConUsuarioYSaldo() throws Exception {
        String json = "{\"usuario\": \"u-123\", \"saldo\": 50000}";
        RequestSaldo dto = objectMapper.readValue(json, RequestSaldo.class);

        assertEquals("u-123", dto.getUsuario());
        assertEquals("u-123", dto.getIdUsuario());
        assertEquals(50000L, dto.getSaldo());
        assertEquals(50000L, dto.getMonto());
    }

    @Test
    void deserializarConIdUsuarioYMonto() throws Exception {
        String json = "{\"idUsuario\": \"u-456\", \"monto\": 25000}";
        RequestSaldo dto = objectMapper.readValue(json, RequestSaldo.class);

        assertEquals("u-456", dto.getUsuario());
        assertEquals("u-456", dto.getIdUsuario());
        assertEquals(25000L, dto.getSaldo());
        assertEquals(25000L, dto.getMonto());
    }

    @Test
    void settersYGettersCompatibilidad() {
        RequestSaldo dto = new RequestSaldo();
        dto.setIdUsuario("u-789");
        dto.setMonto(15000L);

        assertEquals("u-789", dto.getUsuario());
        assertEquals("u-789", dto.getIdUsuario());
        assertEquals(15000L, dto.getSaldo());
        assertEquals(15000L, dto.getMonto());

        dto.setUsuario("u-999");
        dto.setSaldo(30000L);

        assertEquals("u-999", dto.getIdUsuario());
        assertEquals(30000L, dto.getMonto());
    }
}
