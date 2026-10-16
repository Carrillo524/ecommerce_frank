package com.ecommerce.catalogo.infraestructure.driver_adapters.Carrito;

import com.ecommerce.catalogo.domain.model.Carrito;
import com.ecommerce.catalogo.domain.model.gateway.CarritoGateway;
import com.ecommerce.catalogo.infraestructure.mapper.CarritoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarritoGatewayImp implements CarritoGateway {

    private final JpaCarritoDataRepository repository;
    private final CarritoMapper carritoMapper;

    @Override
    public Carrito guardarCarrito(Carrito carrito) {
        CarritoData data = carritoMapper.toData(carrito);
        CarritoData guardado = repository.save(data);
        return carritoMapper.toDomain(guardado);
    }

    @Override
    public Carrito buscarPorIdUsuario(String idUsuario) {
        return repository.findByUsuarioId(idUsuario)
                .map(carritoMapper::toDomain)
                .orElse(null);
    }

    @Override
    public Carrito actualizarCarrito(Carrito carrito) {
        CarritoData data = carritoMapper.toData(carrito);
        CarritoData guardado = repository.save(data);
        return carritoMapper.toDomain(guardado);
    }
}
