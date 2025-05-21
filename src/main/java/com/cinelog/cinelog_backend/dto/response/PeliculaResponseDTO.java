package com.cinelog.cinelog_backend.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeliculaResponseDTO {
    private Long id;               // ID local (de tu base)
    private int idTmdb;            // ID original de TMDB
    private String titulo;
    private int año;
    private String posterURL;
    private String sinopsis;
    private float puntuacionTMDB;
    private List<String> generos;  // Solo nombres
}
