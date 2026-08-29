package com.ecommerce.auth.infraestructure.driver_adapters;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "usuarios")

public class UsuarioData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idUsuario;
    private String nombre;
    @Column(length = 30, nullable = false)
    private String email;
    @Column(length = 12, nullable = false, unique = true)
    private String pass;
    private String role;
    private Integer edad;
    private String numeroTelefono;
}
