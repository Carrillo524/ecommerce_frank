package com.ecommerce.auth.domain.model.gateway;

public interface EncryptGateway {

    String encrypt(String password);

    Boolean checkPass(String passUser);
}