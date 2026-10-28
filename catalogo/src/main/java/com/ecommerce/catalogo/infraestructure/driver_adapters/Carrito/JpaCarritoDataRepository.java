package com.ecommerce.catalogo.infraestructure.driver_adapters.Carrito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaCarritoDataRepository extends JpaRepository<CarritoData, String> {
    Optional<CarritoData> findByUsuarioId(String usuarioId);
}
