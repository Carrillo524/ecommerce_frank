package com.ecommerce.catalogo.domain.usecase;

import com.ecommerce.catalogo.domain.model.Producto;
import com.ecommerce.catalogo.domain.model.gateway.ProductoGateway;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class ProductoUseCase {

    private final ProductoGateway productoGateway;

    public Producto crearProducto(Producto producto) {
        validarProducto(producto);
        return productoGateway.guardarProducto(producto);
    }

    public Producto obtenerPorId(String id) {
        Producto producto = productoGateway.buscarPorId(id);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }
        return producto;
    }

    public List<Producto> listarProductos() {
        return productoGateway.listarProductos();
    }

    public Producto actualizarProducto(Producto producto) {
        if (producto.getId() == null || producto.getId().isBlank()) {
            throw new IllegalArgumentException("El ID del producto es obligatorio para actualizar");
        }
        validarProducto(producto);

        // Verificamos existencia previa
        obtenerPorId(producto.getId());
        return productoGateway.actualizarProducto(producto);
    }

    private void validarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (producto.getPrecio().precision() > 38 || (producto.getPrecio().precision() - producto.getPrecio().scale()) > 36) {
            throw new IllegalArgumentException("El precio supera el límite numérico permitido (máximo 38 dígitos de precisión)");
        }
        if (producto.getStock() == null || producto.getStock() <= 0) {
            throw new IllegalArgumentException("El stock no puede ser cero, negativo o nulo");
        }
    }
}
