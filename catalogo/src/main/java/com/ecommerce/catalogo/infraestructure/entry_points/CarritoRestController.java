package com.ecommerce.catalogo.infraestructure.entry_points;

import com.ecommerce.catalogo.domain.model.Carrito;
import com.ecommerce.catalogo.domain.usecase.CarritoUseCase;
import com.ecommerce.catalogo.infraestructure.entry_points.dto.AgregarItemRequest;
import com.ecommerce.catalogo.infraestructure.entry_points.dto.EliminarItemRequest;
import com.ecommerce.catalogo.infraestructure.entry_points.dto.UsuarioIdRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ecommerce/carrito")
@RequiredArgsConstructor
public class CarritoRestController {

    private final CarritoUseCase carritoUseCase;

    @PostMapping("/crearcarro")
    public ResponseEntity<Carrito> crearCarrito(@RequestBody UsuarioIdRequest request) {
        String usuarioId = request != null ? request.getUsuarioId() : null;
        Carrito nuevoCarrito = carritoUseCase.crearCarrito(usuarioId);
        return ResponseEntity.ok(nuevoCarrito);
    }

    @GetMapping("/buscarcarro/{usuarioId}")
    public ResponseEntity<Carrito> consultarCarrito(@PathVariable String usuarioId) {
        Carrito carrito = carritoUseCase.obtenerCarritoPorUsuario(usuarioId);
        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/llenar")
    public ResponseEntity<Carrito> agregarProducto(@RequestBody AgregarItemRequest request) {
        Carrito carritoActualizado = carritoUseCase.agregarProducto(
                request.getUsuarioId(),
                request.getProductoId(),
                request.getCantidad()
        );
        return ResponseEntity.ok(carritoActualizado);
    }

    @DeleteMapping("/{usuarioId}/productos/{productoId}")
    public ResponseEntity<Carrito> eliminarProducto(
            @PathVariable String usuarioId,
            @PathVariable String productoId) {
        Carrito carritoActualizado = carritoUseCase.eliminarProducto(usuarioId, productoId);
        return ResponseEntity.ok(carritoActualizado);
    }

    @PostMapping("/comprar")
    public ResponseEntity<String> procesarCompra(@RequestBody UsuarioIdRequest request) {
        carritoUseCase.procesarCompra(request != null ? request.getUsuarioId() : null);
        return ResponseEntity.ok("Compra realizada con éxito");
    }
}
