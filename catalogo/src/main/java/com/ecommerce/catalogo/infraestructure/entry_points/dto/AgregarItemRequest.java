package com.ecommerce.catalogo.infraestructure.entry_points.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgregarItemRequest {
    private String usuarioId;
    private String productoId;
    private Integer cantidad;
}
