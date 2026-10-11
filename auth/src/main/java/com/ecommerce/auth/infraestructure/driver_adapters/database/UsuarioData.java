package com.ecommerce.auth.infraestructure.driver_adapters.database;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String idUsuario;
    private String nombre;
    @Column(length = 30, nullable = false)
    private String email;
    @Column(length = 60, nullable = false, unique = true)
    private String pass;
    private String role;
    private Integer edad;
    private String numeroTelefono;
    @Column(nullable = false)
    private Long saldo;
}
