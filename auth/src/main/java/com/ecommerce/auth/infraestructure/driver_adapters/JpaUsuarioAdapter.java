package com.ecommerce.auth.infraestructure.driver_adapters;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import com.ecommerce.auth.infraestructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
        return repository.findByEmail(email).map(mapper::toUsuario)
                .orElseThrow(() -> new RuntimeException("Fallo consulta de base de datos"));
    }
}
