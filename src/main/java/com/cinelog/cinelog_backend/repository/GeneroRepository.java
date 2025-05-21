package com.cinelog.cinelog_backend.repository;

import com.cinelog.cinelog_backend.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
    Optional<Genero> findByIdTmdb(int idTmdb);
}
