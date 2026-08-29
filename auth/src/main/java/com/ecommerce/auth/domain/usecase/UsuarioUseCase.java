package com.ecommerce.auth.domain.usecase;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario == null || Stream.of(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPass(),
                usuario.getRole(),
                usuario.getEdad(),
                usuario.getNumeroTelefono()
        ).anyMatch(Objects::isNull)) {
            throw new NullPointerException("Uy zonas, alguno de los datos son nulos");
        }

        //Quedan pendientes otras validaciones como edad ><= etc.

        return usuarioGateway.guardarUsuario(usuario);
    }
}

