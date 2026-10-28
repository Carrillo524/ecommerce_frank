package com.ecommerce.auth.infraestructure.driver_adapters.database;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import com.ecommerce.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

//IMPLEMEMTAR USUARIO GATEWAY
@Repository
@RequiredArgsConstructor
public class JpaUsuarioAdapter implements UsuarioGateway {

    private final UsuarioDataJpaRepository repository;
    private final UsuarioMapper mapper;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        return mapper.toUsuario(repository.save(mapper.toUsuarioData(usuario)));
    }

    @Override
    public Usuario buscarPorId(String idUsuario) {
        return repository.findById(idUsuario).map(mapper::toUsuario).orElse(null);
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        return mapper.toUsuario(repository.save(mapper.toUsuarioData(usuario)));
    }

    @Override
    public void eliminarPorId(String idUsuario) {
        repository.deleteById(idUsuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email).map(mapper::toUsuario).orElse(null);
    }

    @Override
    public Usuario actualizarSaldo(String idUsuario, BigDecimal nuevoSaldo) {
        return repository.findById(idUsuario).map(usuarioData -> {
            usuarioData.setSaldo(nuevoSaldo);
            return mapper.toUsuario(repository.save(usuarioData));
        }).orElse(null);
    }
}
