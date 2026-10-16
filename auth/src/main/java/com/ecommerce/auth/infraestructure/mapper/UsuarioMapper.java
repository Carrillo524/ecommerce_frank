package com.ecommerce.auth.infraestructure.mapper;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.infraestructure.driver_adapters.database.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toUsuario(UsuarioData usuarioData) {
        if (usuarioData == null) {
            return null;
        }
        return new Usuario(
                usuarioData.getIdUsuario(),
                usuarioData.getNombre(),
                usuarioData.getEmail(),
                usuarioData.getPass(),
                usuarioData.getRole(),
                usuarioData.getEdad(),
                usuarioData.getNumeroTelefono(),
                usuarioData.getSaldo()
        );
    }

    public UsuarioData toUsuarioData(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioData(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPass(),
                usuario.getRole(),
                usuario.getEdad(),
                usuario.getNumeroTelefono(),
                usuario.getSaldo()
        );
    }
}
