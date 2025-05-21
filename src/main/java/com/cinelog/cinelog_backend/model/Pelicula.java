package com.cinelog.cinelog_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int idTmdb; // ID original de TMDB

    private String titulo;

    private int año;

    private String posterURL;

    @Column(columnDefinition = "TEXT")
    private String sinopsis;

    private float puntuacionTMDB;
    @ManyToMany
    @JoinTable(
            name = "pelicula_genero",
            joinColumns = @JoinColumn(name = "pelicula_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    private List<Genero> generos;

}
