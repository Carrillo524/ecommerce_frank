package com.ecommerce.catalogo.infraestructure.driver_adapters.Carrito;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CARRITOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarritoData {

    @Id
    private String id;

    @Column(name = "usuario_id", nullable = false, unique = true)
    private String usuarioId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carrito_id")
    private List<ProductoCarritoData> items = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal total;
}
