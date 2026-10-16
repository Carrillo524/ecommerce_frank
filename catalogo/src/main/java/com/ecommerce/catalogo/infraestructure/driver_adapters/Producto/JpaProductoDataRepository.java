package com.ecommerce.catalogo.infraestructure.driver_adapters.Producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductoDataRepository extends JpaRepository<ProductoData, String> {
}
