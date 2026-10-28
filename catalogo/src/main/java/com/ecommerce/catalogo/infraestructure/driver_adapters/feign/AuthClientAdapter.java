package com.ecommerce.catalogo.infraestructure.driver_adapters.feign;

import com.ecommerce.catalogo.domain.model.gateway.UsuarioAuthGateway;
import com.ecommerce.catalogo.infraestructure.driver_adapters.feign.dto.RequestMovimiento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AuthClientAdapter implements UsuarioAuthGateway {

    private final AuthFeignClient authFeignClient;

    @Override
    public void debitarSaldo(String usuarioId, BigDecimal monto) {
        authFeignClient.registrarMovimiento(new RequestMovimiento(usuarioId, "DEBITO", monto));
    }

    @Override
    public boolean existeUsuario(String usuarioId) {
        try {
            var respuesta = authFeignClient.obtenerUsuarioPorId(usuarioId);
            return respuesta != null && respuesta.getBody() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
