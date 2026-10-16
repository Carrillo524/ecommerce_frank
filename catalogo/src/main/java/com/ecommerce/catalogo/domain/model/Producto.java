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
public class Producto {
    private String id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
}
