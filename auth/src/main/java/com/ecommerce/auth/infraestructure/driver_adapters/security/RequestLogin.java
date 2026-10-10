package com.ecommerce.auth.infraestructure.driver_adapters.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestLogin {
    private String email;
    private String pass;
}
