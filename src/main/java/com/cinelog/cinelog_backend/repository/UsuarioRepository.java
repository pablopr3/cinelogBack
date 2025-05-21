package com.cinelog.cinelog_backend.repository;

import com.cinelog.cinelog_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    Optional<Usuario> findByTokenActivacion(String tokenActivacion);
    Optional<Usuario> findByTokenRecuperacion(String tokenRecuperacion);



}
