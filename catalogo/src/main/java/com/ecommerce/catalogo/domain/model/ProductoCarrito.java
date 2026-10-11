package com.ecommerce.catalogo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.nio.file.LinkOption;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCarrito {
    private String id;
    private String productoId;
    private String nombre;
    private Long precioUnitario;
    private int cantidad;
    private Long subtotal;
}