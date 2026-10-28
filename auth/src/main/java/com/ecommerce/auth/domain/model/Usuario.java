package com.ecommerce.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario {

    private String idUsuario;
    private String nombre;
    private String email;
    private String pass;
    private String role;
    private Integer edad;
    private String numeroTelefono;
    private BigDecimal saldo;
}
