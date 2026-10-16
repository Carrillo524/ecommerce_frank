package com.ecommerce.auth.infraestructure.entry_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import com.ecommerce.auth.infraestructure.driver_adapters.security.RequestLogin;
import com.ecommerce.auth.infraestructure.driver_adapters.database.UsuarioData;
import com.ecommerce.auth.infraestructure.entry_points.Dto.RequestSaldo;
import com.ecommerce.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//RECIBE LAS PETICIONES HTTP
//API ES LA QUE PERMITE CONSULTAS A LA BASE DE DATOS
@RestController
@RequestMapping("/api/ecommerce/usuarios")
@RequiredArgsConstructor
public class UsuarioRestController {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/save")
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody UsuarioData usuarioData) {
        Usuario usuario = usuarioUseCase.guardarUsuario(usuarioMapper.toUsuario(usuarioData));
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @GetMapping("/trae/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable String id) {
        Usuario usuario = usuarioUseCase.obtenerUsuarioPorId(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/mod/")
    public ResponseEntity<Usuario> actualizarUsuario(@RequestBody Usuario usuario) {
        Usuario actualizado = usuarioUseCase.actualizarUsuario(usuario);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/supr/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String id) {
        if (usuarioUseCase.obtenerUsuarioPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioUseCase.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@RequestBody RequestLogin requestLogin) {
        try {
            String respuesta = usuarioUseCase.loginUsuario(requestLogin.getEmail(), requestLogin.getPass());
            return ResponseEntity.ok(respuesta);
        } catch (SecurityException error) {
            return ResponseEntity.status(HttpStatus.OK).body(error.getMessage());
        }
    }

    @PatchMapping("/debitar")
    public ResponseEntity<String> debitarSaldo(@RequestBody RequestSaldo requestSaldo) {
        try {
            usuarioUseCase.debitarSaldo(requestSaldo.getIdUsuario(), requestSaldo.getMonto());
            return ResponseEntity.ok("Saldo debitado exitosamente");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/recargar")
    public ResponseEntity<String> recargarSaldo(@RequestBody RequestSaldo requestSaldo) {
        try {
            usuarioUseCase.recargarSaldo(requestSaldo.getIdUsuario(), requestSaldo.getMonto());
            return ResponseEntity.ok("Saldo recargado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/actualizarSaldo")
    public ResponseEntity<Usuario> actualizarSaldo(@RequestBody RequestSaldo requestSaldo) {
        if (requestSaldo == null) {
            throw new IllegalArgumentException("El cuerpo de la solicitud no puede ser nulo");
        }
        String idUsuario = requestSaldo.getIdUsuario();
        Usuario actualizado = usuarioUseCase.actualizarSaldo(idUsuario, requestSaldo.getMonto());
        return ResponseEntity.ok(actualizado);
    }
}
