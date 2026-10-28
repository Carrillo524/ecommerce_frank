package com.ecommerce.catalogo.infraestructure.driver_adapters.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestMovimiento {
    private String idUsuario;
    private String tipo;
    private BigDecimal monto;
}
