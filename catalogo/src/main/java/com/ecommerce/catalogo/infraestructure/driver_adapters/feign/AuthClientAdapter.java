package com.ecommerce.catalogo.infraestructure.driver_adapters.feign;

import com.ecommerce.catalogo.domain.model.gateway.UsuarioAuthGateway;
import com.ecommerce.catalogo.infraestructure.driver_adapters.feign.dto.DebitarSaldoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AuthClientAdapter implements UsuarioAuthGateway {

    private final AuthFeignClient authFeignClient;

    @Override
    public void debitarSaldo(String usuarioId, BigDecimal monto) {
        authFeignClient.debitarSaldo(new DebitarSaldoRequest(usuarioId, monto));
    }

    @Override
    public boolean existeUsuario(String usuarioId){
        return authFeignClient.obtenerUsuarioPorId(usuarioId) != null;
    }
}
