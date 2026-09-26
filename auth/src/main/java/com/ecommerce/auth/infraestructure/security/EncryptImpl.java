package com.ecommerce.auth.infraestructure.security;

import com.ecommerce.auth.domain.model.gateway.EncryptGateway;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptImpl implements EncryptGateway {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encrypt(String password) {
        return encoder.encode(password);
    }

    @Override
    public Boolean checkPass(String passUser) {
        return encoder.matches(passUser,  encrypt(passUser));
    }
}