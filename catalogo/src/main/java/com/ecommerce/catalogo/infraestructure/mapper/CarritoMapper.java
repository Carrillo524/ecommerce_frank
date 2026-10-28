package com.ecommerce.catalogo.infraestructure.mapper;

import com.ecommerce.catalogo.domain.model.Carrito;
import com.ecommerce.catalogo.domain.model.ProductoCarrito;
import com.ecommerce.catalogo.infraestructure.driver_adapters.Carrito.CarritoData;
import com.ecommerce.catalogo.infraestructure.driver_adapters.Carrito.ProductoCarritoData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarritoMapper {

    public CarritoData toData(Carrito carrito) {
        if (carrito == null) {
            return null;
        }

        List<ProductoCarritoData> itemsData = carrito.getItems() != null
                ? carrito.getItems().stream()
                        .map(this::toItemData)
                        .collect(Collectors.toCollection(ArrayList::new))
                : new ArrayList<>();

        return new CarritoData(
                carrito.getId(),
                carrito.getUsuarioId(),
                itemsData,
                carrito.getTotal()
        );
    }

    public Carrito toDomain(CarritoData data) {
        if (data == null) {
            return null;
        }

        List<ProductoCarrito> itemsDomain = data.getItems() != null
                ? data.getItems().stream()
                        .map(this::toItemDomain)
                        .collect(Collectors.toCollection(ArrayList::new))
                : new ArrayList<>();

        return Carrito.builder()
                .id(data.getId())
                .usuarioId(data.getUsuarioId())
                .items(itemsDomain)
                .total(data.getTotal())
                .build();
    }

    public ProductoCarritoData toItemData(ProductoCarrito item) {
        if (item == null) {
            return null;
        }

        return new ProductoCarritoData(
                item.getId(),
                item.getProductoId(),
                item.getNombre(),
                item.getPrecioUnitario(),
                item.getCantidad(),
                item.getSubtotal()
        );
    }

    public ProductoCarrito toItemDomain(ProductoCarritoData itemData) {
        if (itemData == null) {
            return null;
        }

        return ProductoCarrito.builder()
                .id(itemData.getId())
                .productoId(itemData.getProductoId())
                .nombre(itemData.getNombre())
                .precioUnitario(itemData.getPrecioUnitario())
                .cantidad(itemData.getCantidad())
                .subtotal(itemData.getSubtotal())
                .build();
    }
}
