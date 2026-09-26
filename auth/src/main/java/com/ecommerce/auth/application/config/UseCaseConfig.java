package com.ecommerce.auth.application.config;

import com.ecommerce.auth.domain.model.gateway.EncryptGateway;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateway usuarioGateway, EncryptGateway encryptGateway) {
        return new UsuarioUseCase(usuarioGateway, encryptGateway);
    }
}