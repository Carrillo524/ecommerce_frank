package com.ecommerce.catalogo.domain.model.gateway;

import com.ecommerce.catalogo.domain.model.Carrito;

public interface CarritoGateway {
    Carrito guardarCarrito(Carrito carrito);
    Carrito buscarPorIdUsuario(String idUsuario);
    Carrito actualizarCarrito(Carrito carrito);
}