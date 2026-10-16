package com.ecommerce.catalogo.infraestructure.driver_adapters.feign;

import com.ecommerce.catalogo.infraestructure.driver_adapters.feign.dto.DebitarSaldoRequest;
import com.ecommerce.catalogo.infraestructure.entry_points.dto.UsuarioIdRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "${services.auth.url}")
public interface AuthFeignClient {

    @PatchMapping("/debitar")
    ResponseEntity<String> debitarSaldo(@RequestBody DebitarSaldoRequest request);

    @GetMapping("/trae/{id}")
    ResponseEntity<UsuarioIdRequest> obtenerUsuarioPorId(@PathVariable("id") String id);
}
