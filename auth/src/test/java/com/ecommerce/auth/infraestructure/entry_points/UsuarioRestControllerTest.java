package com.ecommerce.auth.infraestructure.entry_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import com.ecommerce.auth.infraestructure.mapper.UsuarioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsuarioRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioUseCase usuarioUseCase;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioRestController usuarioRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioRestController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void actualizarSaldo_Exitoso() throws Exception {
        String idUsuario = "user-100";
        Long nuevoSaldo = 45000L;
        Usuario usuarioActualizado = new Usuario(idUsuario, "Carlos", "carlos@test.com", "pass", "CLIENTE", 28, "3100000000", nuevoSaldo);

        when(usuarioUseCase.actualizarSaldo(idUsuario, nuevoSaldo)).thenReturn(usuarioActualizado);

        String jsonBody = "{\"usuario\": \"" + idUsuario + "\", \"saldo\": " + nuevoSaldo + "}";

        mockMvc.perform(patch("/api/ecommerce/usuarios/saldo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(idUsuario))
                .andExpect(jsonPath("$.saldo").value(nuevoSaldo));

        verify(usuarioUseCase, times(1)).actualizarSaldo(idUsuario, nuevoSaldo);
    }

    @Test
    void actualizarSaldo_ConIdUsuarioYMonto_Exitoso() throws Exception {
        String idUsuario = "user-200";
        Long nuevoSaldo = 60000L;
        Usuario usuarioActualizado = new Usuario(idUsuario, "Laura", "laura@test.com", "pass", "CLIENTE", 22, "3200000000", nuevoSaldo);

        when(usuarioUseCase.actualizarSaldo(idUsuario, nuevoSaldo)).thenReturn(usuarioActualizado);

        String jsonBody = "{\"idUsuario\": \"" + idUsuario + "\", \"monto\": " + nuevoSaldo + "}";

        mockMvc.perform(patch("/api/ecommerce/usuarios/actualizar-saldo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(idUsuario))
                .andExpect(jsonPath("$.saldo").value(nuevoSaldo));

        verify(usuarioUseCase, times(1)).actualizarSaldo(idUsuario, nuevoSaldo);
    }

    @Test
    void actualizarSaldo_Retorna400CuandoSaldoEsNegativo() throws Exception {
        String idUsuario = "user-100";
        when(usuarioUseCase.actualizarSaldo(idUsuario, -500L))
                .thenThrow(new IllegalArgumentException("El saldo no puede ser negativo"));

        String jsonBody = "{\"usuario\": \"" + idUsuario + "\", \"saldo\": -500}";

        mockMvc.perform(patch("/api/ecommerce/usuarios/saldo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El saldo no puede ser negativo"));
    }

    @Test
    void actualizarSaldo_Retorna404CuandoUsuarioNoExiste() throws Exception {
        String idUsuario = "user-inexistente";
        when(usuarioUseCase.actualizarSaldo(idUsuario, 1000L))
                .thenThrow(new IllegalArgumentException("Usuario no encontrado con ID: " + idUsuario));

        String jsonBody = "{\"usuario\": \"" + idUsuario + "\", \"saldo\": 1000}";

        mockMvc.perform(patch("/api/ecommerce/usuarios/saldo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuario no encontrado con ID: " + idUsuario));
    }
}
