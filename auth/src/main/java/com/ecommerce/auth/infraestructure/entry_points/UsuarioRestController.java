package com.ecommerce.auth.infraestructure.entry_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioRestController {

    private final UsuarioUseCase usuarioUseCase;

    @PostMapping
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario) {
        return new ResponseEntity<>(usuarioUseCase.guardarUsuario(usuario), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable String id) {
        Usuario usuario = usuarioUseCase.obtenerUsuarioPorId(id);

        // CORRECCIÓN: Se valida la entidad recuperada de la BD
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable String id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioUseCase.actualizarUsuario(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String id) {
        if (usuarioUseCase.obtenerUsuarioPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioUseCase.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}