package com.ecommerce.auth.domain.usecase;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.EncryptGateway;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final EncryptGateway encryptGateway;

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario != null && usuario.getSaldo() == null) {
            usuario.setSaldo(BigDecimal.ZERO);
        }

        validarUsuario(usuario);

        if (usuarioGateway.buscarPorEmail(usuario.getEmail()) != null) {
            return null;
        }

        String passCrypt = encryptGateway.encrypt(usuario.getPass());
        usuario.setPass(passCrypt);

        return usuarioGateway.guardarUsuario(usuario);
    }

    public Usuario obtenerUsuarioPorId(String idUsuario) {
        if (idUsuario == null || idUsuario.isBlank()) {
            return null;
        }
        return usuarioGateway.buscarPorId(idUsuario);
    }

    public Usuario actualizarUsuario(Usuario usuario) {

        if (usuario == null || usuario.getIdUsuario() == null || usuario.getIdUsuario().isBlank()) {
            return null;
        }

        validarUsuario(usuario);

        // Verificamos existencia antes de actualizar
        Usuario usuarioExistente = obtenerUsuarioPorId(usuario.getIdUsuario());
        if (usuarioExistente == null) {
            return null;
        }

        Usuario usuarioConMismoEmail = usuarioGateway.buscarPorEmail(usuario.getEmail());
        if (usuarioConMismoEmail != null && !usuarioConMismoEmail.getIdUsuario().equals(usuario.getIdUsuario())) {
            return null;
        }

        // Si la clave que viene es distinta a la que ya tiene en BD, la encriptamos
        if (!usuario.getPass().equals(usuarioExistente.getPass())) {
            usuario.setPass(encryptGateway.encrypt(usuario.getPass()));
        }

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
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPass(),
                usuario.getRole(),
                usuario.getEdad(),
                usuario.getNumeroTelefono(),
                usuario.getSaldo()
        ).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Uy zonas, alguno de los datos son nulos");
        }

        if (usuario.getEdad() < 18) {
            throw new IllegalArgumentException("El usuario debe tener 18 años o más");
        }

        if (usuario.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo no puede ser negativo");
        }
    }

    public String loginUsuario(String email, String password) {
        Usuario usuarioLogin = usuarioGateway.buscarPorEmail(email);

        // Verificar que el email exista y que la clave coincida
        if (usuarioLogin != null && Boolean.TRUE.equals(encryptGateway.checkPass(password, usuarioLogin.getPass()))) {
            return "Login exitoso";
        }

        // Si no existe o la contraseña está mal, retorna siempre lo mismo con 200 OK
        return "credenciales invalidas";

    }

    public Usuario debitarSaldo(String idUsuario, BigDecimal monto) {
        validarMonto(monto);

        Usuario usuario = obtenerUsuarioPorId(idUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + idUsuario);
        }

        if (usuario.getSaldo().compareTo(monto) < 0) {
            throw new IllegalStateException("Fondos insuficientes: el saldo actual (" + usuario.getSaldo() + ") es menor al monto a debitar (" + monto + ")");
        }

        BigDecimal nuevoSaldo = usuario.getSaldo().subtract(monto);
        usuario.setSaldo(nuevoSaldo);
        Usuario actualizado = usuarioGateway.actualizarSaldo(idUsuario, nuevoSaldo);
        return actualizado != null ? actualizado : usuario;
    }


    public Usuario recargarSaldo(String idUsuario, BigDecimal monto) {
        validarMonto(monto);


        Usuario usuario = obtenerUsuarioPorId(idUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + idUsuario);
        }

        BigDecimal nuevoSaldo = usuario.getSaldo().add(monto);
        usuario.setSaldo(nuevoSaldo);
        Usuario actualizado = usuarioGateway.actualizarSaldo(idUsuario, nuevoSaldo);
        return actualizado != null ? actualizado : usuario;
    }

    private void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto no puede ser menor o igual a 0 o nulo");
        }
        if (monto.precision() > 38 || (monto.precision() - monto.scale()) > 36) {
            throw new IllegalArgumentException("El monto supera el límite numérico permitido (máximo 38 dígitos)");
        }
    }


}
