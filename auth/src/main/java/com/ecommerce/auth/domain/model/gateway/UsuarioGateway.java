package com.ecommerce.auth.domain.model.gateway;

import com.ecommerce.auth.domain.model.Usuario;

public interface UsuarioGateway {
    //Contratos API
    Usuario guardarUsuario(Usuario usuario);
    Usuario buscarPorId(String idUsuario);
    Usuario actualizarUsuario(Usuario usuario);
    void eliminarPorId(String idUsuario);
}
