package com.ecommerce.catalogo.infraestructure.entry_points;

import com.ecommerce.catalogo.domain.model.Producto;
import com.ecommerce.catalogo.domain.usecase.ProductoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/productos")
@RequiredArgsConstructor
public class ProductoRestController {

    private final ProductoUseCase productoUseCase;

    @PostMapping("/crear")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto productoCreado = productoUseCase.crearProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoCreado);
    }

    @GetMapping("/mostrar")
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> lista = productoUseCase.listarProductos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/mostrar/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable String id) {
        Producto producto = productoUseCase.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/cambiar")
    public ResponseEntity<Producto> actualizarProducto(@RequestBody Producto producto) {
        producto.setId(producto.getId());
        Producto productoActualizado = productoUseCase.actualizarProducto(producto);
        return ResponseEntity.ok(productoActualizado);
    }
}
