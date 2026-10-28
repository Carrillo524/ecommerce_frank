package com.ecommerce.catalogo.infraestructure.driver_adapters.Carrito;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCTO_CARRITO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCarritoData {

    @Id
    private String id;

    @Column(name = "producto_id", nullable = false)
    private String productoId;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private BigDecimal subtotal;
}
