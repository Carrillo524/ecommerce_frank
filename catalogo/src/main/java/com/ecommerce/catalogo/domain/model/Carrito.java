package com.ecommerce.catalogo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {
    private String id;
    private String usuarioId;
    private List<ProductoCarrito> items;
    private BigDecimal total;
}
