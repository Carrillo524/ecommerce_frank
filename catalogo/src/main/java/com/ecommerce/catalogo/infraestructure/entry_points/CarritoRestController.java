package com.ecommerce.catalogo.infraestructure.entry_points;

import com.ecommerce.catalogo.domain.model.Carrito;
import com.ecommerce.catalogo.domain.usecase.CarritoUseCase;
import com.ecommerce.catalogo.infraestructure.entry_points.dto.AgregarItemRequest;
import com.ecommerce.catalogo.infraestructure.entry_points.dto.EliminarItemRequest;
import com.ecommerce.catalogo.infraestructure.entry_points.dto.UsuarioIdRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<Carrito> obtenerCarritoPorUsuario(@RequestBody UsuarioIdRequest request) {
        Carrito carrito = carritoUseCase.obtenerCarritoPorUsuario(request != null ? request.getUsuarioId() : null);
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

    @DeleteMapping("/quitarproducto")
    public ResponseEntity<Carrito> eliminarProducto(@RequestBody EliminarItemRequest request) {
        Carrito carritoActualizado = carritoUseCase.eliminarProducto(
                request.getUsuarioId(),
                request.getProductoId()
        );
        return ResponseEntity.ok(carritoActualizado);
    }

    @PostMapping("/comprar")
    public ResponseEntity<String> procesarCompra(@RequestBody UsuarioIdRequest request) {
        carritoUseCase.procesarCompra(request != null ? request.getUsuarioId() : null);
        return ResponseEntity.ok("Compra realizada con éxito");
    }
}
