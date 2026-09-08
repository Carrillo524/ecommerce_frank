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
        validarUsuario(usuario);
        return usuarioGateway.guardarUsuario(usuario);
    }

    public Usuario obtenerUsuarioPorId(String idUsuario) {
        if (idUsuario == null || idUsuario.isBlank()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo o vacío");
        }
        Usuario usuario = usuarioGateway.buscarPorId(idUsuario);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }
        return usuario;
    }

    public Usuario actualizarUsuario(String idUsuario, Usuario usuario) {
        if (idUsuario == null || !idUsuario.equals(usuario.getIdUsuario())) {
            throw new IllegalArgumentException("El ID del usuario no coincide");
        }
        validarUsuario(usuario);
        
        // Verificamos existencia antes de actualizar
        obtenerUsuarioPorId(idUsuario);
        
        return usuarioGateway.actualizarUsuario(usuario);
    }

    public void eliminarUsuario(String idUsuario) {
        if (idUsuario == null || idUsuario.isBlank()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo o vacío");
        }
        // Verificamos existencia antes de eliminar
        obtenerUsuarioPorId(idUsuario);
        usuarioGateway.eliminarPorId(idUsuario);
    }

    private void validarUsuario(Usuario usuario) {
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
    }

}

