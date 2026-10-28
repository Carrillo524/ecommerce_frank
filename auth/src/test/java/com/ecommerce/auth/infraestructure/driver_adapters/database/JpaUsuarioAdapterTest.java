package com.ecommerce.auth.infraestructure.driver_adapters.database;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.infraestructure.mapper.UsuarioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaUsuarioAdapterTest {

    @Mock
    private UsuarioDataJpaRepository repository;

    private UsuarioMapper mapper;
    private JpaUsuarioAdapter adapter;

    @BeforeEach
    void setUp() {
        mapper = new UsuarioMapper();
        adapter = new JpaUsuarioAdapter(repository, mapper);
    }

    @Test
    void actualizarSaldo_Exitoso() {
        String idUsuario = "u-1";
        Long nuevoSaldo = 75000L;

        UsuarioData usuarioData = new UsuarioData(idUsuario, "Ana", "ana@test.com", "pass", "ADMIN", 30, "3110000000", 20000L);
        UsuarioData usuarioDataActualizado = new UsuarioData(idUsuario, "Ana", "ana@test.com", "pass", "ADMIN", 30, "3110000000", nuevoSaldo);

        when(repository.findById(idUsuario)).thenReturn(Optional.of(usuarioData));
        when(repository.save(any(UsuarioData.class))).thenReturn(usuarioDataActualizado);

        Usuario resultado = adapter.actualizarSaldo(idUsuario, nuevoSaldo);

        assertNotNull(resultado);
        assertEquals(nuevoSaldo, resultado.getSaldo());
        verify(repository, times(1)).findById(idUsuario);
        verify(repository, times(1)).save(any(UsuarioData.class));
    }

    @Test
    void actualizarSaldo_RetornaNullSiNoExiste() {
        String idUsuario = "u-not-found";
        when(repository.findById(idUsuario)).thenReturn(Optional.empty());

        Usuario resultado = adapter.actualizarSaldo(idUsuario, 5000L);

        assertNull(resultado);
        verify(repository, times(1)).findById(idUsuario);
        verify(repository, never()).save(any());
    }
}
