package com.ecommerce.catalogo.domain.usecase;

import com.ecommerce.catalogo.domain.model.Carrito;
import com.ecommerce.catalogo.domain.model.Producto;
import com.ecommerce.catalogo.domain.model.ProductoCarrito;
import com.ecommerce.catalogo.domain.model.gateway.CarritoGateway;
import com.ecommerce.catalogo.domain.model.gateway.ProductoGateway;
import com.ecommerce.catalogo.domain.model.gateway.UsuarioAuthGateway;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@RequiredArgsConstructor
public class CarritoUseCase {

    private final CarritoGateway carritoGateway;
    private final ProductoGateway productoGateway;
    private final UsuarioAuthGateway usuarioAuthGateway;

    public Carrito obtenerCarritoPorUsuario(String idUsuario) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es requerido");
        }

        if (!usuarioAuthGateway.existeUsuario(idUsuario)) {
            throw new IllegalArgumentException("El usuario no existe en el sistema");
        }

        Carrito carrito = carritoGateway.buscarPorIdUsuario(idUsuario);
        if (carrito == null) {
            carrito = new Carrito(UUID.randomUUID().toString(), idUsuario, new ArrayList<>(), BigDecimal.ZERO);
            carrito = carritoGateway.guardarCarrito(carrito);
        }
        return carrito;
    }

    public Carrito agregarProducto(String idUsuario, String productoId, Integer cantidad) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es requerido");
        }
        if (productoId == null || productoId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del producto es requerido");
        }
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        Producto producto = productoGateway.buscarPorId(productoId);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + productoId);
        }

        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        ProductoCarrito itemExistente = carrito.getItems().stream()
                .filter(item -> item.getProductoId().equals(productoId))
                .findFirst()
                .orElse(null);

        Integer cantidadTotalDeseada = (itemExistente != null ? itemExistente.getCantidad() : 0) + cantidad;

        if (producto.getStock() < cantidadTotalDeseada) {
            throw new IllegalStateException("Stock insuficiente. Disponible: " + producto.getStock());
        }

        if (itemExistente != null) {
            itemExistente.setCantidad(cantidadTotalDeseada);
            itemExistente.setSubtotal(itemExistente.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidadTotalDeseada)));
        } else {
            ProductoCarrito nuevoItem = new ProductoCarrito(
                    UUID.randomUUID().toString(),
                    producto.getId(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    cantidad,
                    producto.getPrecio().multiply(BigDecimal.valueOf(cantidad))
            );
            carrito.getItems().add(nuevoItem);
        }

        recalcularTotal(carrito);
        return carritoGateway.actualizarCarrito(carrito);
    }

    public Carrito eliminarProducto(String idUsuario, String productoId) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es requerido");
        }
        if (productoId == null || productoId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del producto es requerido");
        }

        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);
        carrito.getItems().removeIf(item -> item.getProductoId().equals(productoId));
        recalcularTotal(carrito);
        return carritoGateway.actualizarCarrito(carrito);
    }

    public void vaciarCarrito(String idUsuario) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es requerido");
        }

        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);
        carrito.getItems().clear();
        carrito.setTotal(BigDecimal.ZERO);
        carritoGateway.actualizarCarrito(carrito);
    }

    public void procesarCompra(String idUsuario) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es requerido");
        }

        Carrito carrito = carritoGateway.buscarPorIdUsuario(idUsuario);
        if (carrito == null || carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío, no se puede procesar la compra");
        }

        for (ProductoCarrito item : carrito.getItems()) {
            Producto producto = productoGateway.buscarPorId(item.getProductoId());
            if (producto == null || producto.getStock() < item.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para el producto: " + item.getNombre());
            }
        }

        usuarioAuthGateway.debitarSaldo(idUsuario, carrito.getTotal());

        for (ProductoCarrito item : carrito.getItems()) {
            Producto producto = productoGateway.buscarPorId(item.getProductoId());
            producto.setStock(producto.getStock() - item.getCantidad());
            productoGateway.actualizarProducto(producto);
        }

        vaciarCarrito(idUsuario);
    }

    private void recalcularTotal(Carrito carrito) {
        BigDecimal total = carrito.getItems().stream()
                .map(ProductoCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        carrito.setTotal(total);
    }
}
