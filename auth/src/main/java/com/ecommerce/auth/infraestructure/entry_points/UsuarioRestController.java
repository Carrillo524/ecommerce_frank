package com.ecommerce.auth.infraestructure.entry_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import com.ecommerce.auth.infraestructure.driver_adapters.UsuarioData;
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
        return new ResponseEntity<>(usuarioUseCase.guardarUsuario(usuarioMapper.toUsuario(usuarioData)), HttpStatus.CREATED);
    }

    @GetMapping("/trae/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable String id) {
        Usuario usuario = usuarioUseCase.obtenerUsuarioPorId(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/mod/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable String id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioUseCase.actualizarUsuario(id, usuario));
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
    public ResponseEntity<?> loginUsuario(@RequestBody UsuarioData usuarioData) {
        try {
            String mensajeRespuesta = usuarioUseCase.loginUsuario(usuarioData.getEmail(), usuarioData.getPass());
            return new ResponseEntity<>(mensajeRespuesta, HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>("Falló el logueo", HttpStatus.CONFLICT);
        }
    }
}