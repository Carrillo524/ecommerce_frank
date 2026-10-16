package com.ecommerce.auth.domain.usecase;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.EncryptGateway;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private EncryptGateway encryptGateway;

    private UsuarioUseCase usuarioUseCase;

    @BeforeEach
    void setUp() {
        usuarioUseCase = new UsuarioUseCase(usuarioGateway, encryptGateway);
    }

    @Test
    void actualizarSaldo_Exitoso() {
        String idUsuario = "user-123";
        Long nuevoSaldo = 50000L;

        Usuario usuarioExistente = new Usuario(idUsuario, "Juan", "juan@test.com", "pass123", "CLIENTE", 25, "3001234567", 10000L);
        Usuario usuarioActualizado = new Usuario(idUsuario, "Juan", "juan@test.com", "pass123", "CLIENTE", 25, "3001234567", nuevoSaldo);

        when(usuarioGateway.buscarPorId(idUsuario)).thenReturn(usuarioExistente);
        when(usuarioGateway.actualizarSaldo(idUsuario, nuevoSaldo)).thenReturn(usuarioActualizado);

        Usuario resultado = usuarioUseCase.actualizarSaldo(idUsuario, nuevoSaldo);

        assertNotNull(resultado);
        assertEquals(nuevoSaldo, resultado.getSaldo());
        verify(usuarioGateway, times(1)).buscarPorId(idUsuario);
        verify(usuarioGateway, times(1)).actualizarSaldo(idUsuario, nuevoSaldo);
    }

    @Test
    void actualizarSaldo_LanzaExcepcionCuandoIdEsNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> usuarioUseCase.actualizarSaldo(null, 1000L));
        assertThrows(IllegalArgumentException.class, () -> usuarioUseCase.actualizarSaldo("", 1000L));
        assertThrows(IllegalArgumentException.class, () -> usuarioUseCase.actualizarSaldo("   ", 1000L));
        verify(usuarioGateway, never()).actualizarSaldo(any(), any());
    }

    @Test
    void actualizarSaldo_LanzaExcepcionCuandoSaldoEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> usuarioUseCase.actualizarSaldo("user-1", null));
        verify(usuarioGateway, never()).actualizarSaldo(any(), any());
    }

    @Test
    void actualizarSaldo_LanzaExcepcionCuandoSaldoEsNegativo() {
        assertThrows(IllegalArgumentException.class, () -> usuarioUseCase.actualizarSaldo("user-1", -100L));
        verify(usuarioGateway, never()).actualizarSaldo(any(), any());
    }

    @Test
    void actualizarSaldo_LanzaExcepcionCuandoUsuarioNoExiste() {
        String idUsuario = "user-inexistente";
        when(usuarioGateway.buscarPorId(idUsuario)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                usuarioUseCase.actualizarSaldo(idUsuario, 2000L)
        );

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
        verify(usuarioGateway, never()).actualizarSaldo(any(), any());
    }

    @Test
    void debitarSaldo_Exitoso() {
        String idUsuario = "user-123";
        Usuario usuarioExistente = new Usuario(idUsuario, "Juan", "juan@test.com", "pass123", "CLIENTE", 25, "3001234567", 10000L);

        when(usuarioGateway.buscarPorId(idUsuario)).thenReturn(usuarioExistente);

        usuarioUseCase.debitarSaldo(idUsuario, 4000L);

        assertEquals(6000L, usuarioExistente.getSaldo());
        verify(usuarioGateway, times(1)).actualizarSaldo(idUsuario, 6000L);
    }
}
