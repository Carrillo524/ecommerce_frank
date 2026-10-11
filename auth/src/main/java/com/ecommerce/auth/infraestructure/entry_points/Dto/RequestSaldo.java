package com.ecommerce.auth.infraestructure.entry_points.Dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestSaldo {
    private String idUsuario;
    private Long monto;
}
