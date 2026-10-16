package com.ecommerce.auth.domain.model.gateway;

import com.ecommerce.auth.domain.model.Usuario;

import java.math.BigDecimal;

public interface UsuarioGateway {
    Usuario guardarUsuario(Usuario usuario);
    Usuario buscarPorId(String idUsuario);
    Usuario actualizarUsuario(Usuario usuario);
    void eliminarPorId(String idUsuario);
    Usuario buscarPorEmail(String email);
    Usuario actualizarSaldo(String idUsuario, BigDecimal nuevoSaldo);
}
