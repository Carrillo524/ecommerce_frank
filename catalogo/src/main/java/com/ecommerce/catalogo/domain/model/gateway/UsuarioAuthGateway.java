package com.ecommerce.catalogo.domain.model.gateway;

import java.math.BigDecimal;

public interface UsuarioAuthGateway {
    void debitarSaldo(String usuarioId, BigDecimal monto);
    boolean existeUsuario(String usuarioId);
}
