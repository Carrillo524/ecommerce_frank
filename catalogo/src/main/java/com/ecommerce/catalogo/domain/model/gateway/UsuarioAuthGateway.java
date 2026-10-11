package com.ecommerce.catalogo.domain.model.gateway;

public interface UsuarioAuthGateway {
    void debitarSaldo(String usuarioId, Long monto);
}