package com.cinelog.cinelog_backend.repository;

import com.cinelog.cinelog_backend.model.EntradaDiario;
import com.cinelog.cinelog_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntradaDiarioRepository extends JpaRepository<EntradaDiario, Long> {

    Optional<EntradaDiario> findByUsuarioIdAndPeliculaId(Long usuarioId, Long peliculaId);

    List<EntradaDiario> findByUsuario(Usuario usuario);

    void deleteAllByUsuario(Usuario usuario);

}
