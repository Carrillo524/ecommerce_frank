package com.ecommerce.catalogo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCarrito {
    private String id;
    private String productoId;
    private String nombre;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;
}
