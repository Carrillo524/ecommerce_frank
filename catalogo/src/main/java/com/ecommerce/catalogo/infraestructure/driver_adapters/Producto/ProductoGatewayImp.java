package com.ecommerce.catalogo.infraestructure.driver_adapters.Producto;

import com.ecommerce.catalogo.domain.model.Producto;
import com.ecommerce.catalogo.domain.model.gateway.ProductoGateway;
import com.ecommerce.catalogo.infraestructure.mapper.ProductoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductoGatewayImp implements ProductoGateway {

    private final JpaProductoDataRepository repository;
    private final ProductoMapper productoMapper;

    @Override
    public Producto guardarProducto(Producto producto) {
        ProductoData data = productoMapper.toData(producto);
        ProductoData guardado = repository.save(data);
        return productoMapper.toDomain(guardado);
    }

    @Override
    public Producto buscarPorId(String id) {
        return repository.findById(id)
                .map(productoMapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<Producto> listarProductos() {
        return repository.findAll()
                .stream()
                .map(productoMapper::toDomain)
                .toList();
    }

    @Override
    public Producto actualizarProducto(Producto producto) {
        ProductoData data = productoMapper.toData(producto);
        ProductoData guardado = repository.save(data);
        return productoMapper.toDomain(guardado);
    }
}
