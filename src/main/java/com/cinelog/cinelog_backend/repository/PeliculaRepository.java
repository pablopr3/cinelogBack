package com.cinelog.cinelog_backend.repository;

import com.cinelog.cinelog_backend.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    Optional<Pelicula> findByIdTmdb(int idTmdb);
}
