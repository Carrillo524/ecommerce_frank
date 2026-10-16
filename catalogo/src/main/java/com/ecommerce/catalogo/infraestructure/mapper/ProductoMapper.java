package com.ecommerce.catalogo.infraestructure.mapper;

import com.ecommerce.catalogo.domain.model.Producto;
import com.ecommerce.catalogo.infraestructure.driver_adapters.Producto.ProductoData;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoData toData(Producto producto) {
        if (producto == null) {
            return null;
        }
        return new ProductoData(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock()
        );
    }

    public Producto toDomain(ProductoData data) {
        if (data == null) {
            return null;
        }
        return Producto.builder()
                .id(data.getId())
                .nombre(data.getNombre())
                .descripcion(data.getDescripcion())
                .precio(data.getPrecio())
                .stock(data.getStock())
                .build();
    }
}
