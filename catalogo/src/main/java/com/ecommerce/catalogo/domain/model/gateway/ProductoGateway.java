package com.ecommerce.catalogo.domain.model.gateway;

import com.ecommerce.catalogo.domain.model.Producto;
import java.util.List;

public interface ProductoGateway {
    Producto guardarProducto(Producto producto);
    Producto buscarPorId(String idProducto);
    List<Producto> listarProductos();
    Producto actualizarProducto(Producto producto);
}