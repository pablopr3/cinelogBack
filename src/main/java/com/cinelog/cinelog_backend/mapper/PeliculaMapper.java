package com.cinelog.cinelog_backend.mapper;

import com.cinelog.cinelog_backend.dto.response.PeliculaResponseDTO;
import com.cinelog.cinelog_backend.model.Pelicula;

import java.util.stream.Collectors;

public class PeliculaMapper {

    public static PeliculaResponseDTO toDTO(Pelicula pelicula) {
        return PeliculaResponseDTO.builder()
                .id(pelicula.getId())
                .idTmdb(pelicula.getIdTmdb())
                .titulo(pelicula.getTitulo())
                .año(pelicula.getAño())
                .posterURL(pelicula.getPosterURL())
                .sinopsis(pelicula.getSinopsis())
                .puntuacionTMDB(pelicula.getPuntuacionTMDB())
                .generos(
                        pelicula.getGeneros().stream()
                                .map(g -> g.getNombre())
                                .collect(Collectors.toList())
                )
                .build();
    }
}
