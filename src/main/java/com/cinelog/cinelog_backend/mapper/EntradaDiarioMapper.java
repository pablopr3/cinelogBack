package com.cinelog.cinelog_backend.mapper;

import com.cinelog.cinelog_backend.dto.response.EntradaDiarioResponseDTO;
import com.cinelog.cinelog_backend.dto.response.PeliculaResponseDTO;
import com.cinelog.cinelog_backend.dto.response.UsuarioMinResponseDTO;
import com.cinelog.cinelog_backend.model.EntradaDiario;
import com.cinelog.cinelog_backend.model.Usuario;

public class EntradaDiarioMapper {

    public static EntradaDiarioResponseDTO toDTO(EntradaDiario entrada) {
        return EntradaDiarioResponseDTO.builder()
                .id(entrada.getId())
                .comentario(entrada.getComentario())
                .puntuacion(entrada.getPuntuacion())
                .favorito(entrada.isFavorito())
                .visto(entrada.isVisto())
                .fechaAgregado(entrada.getFechaAgregado())
                .pelicula(PeliculaMapper.toDTO(entrada.getPelicula()))
                .usuario(toMinDTO(entrada.getUsuario()))
                .build();
    }

    private static UsuarioMinResponseDTO toMinDTO(Usuario usuario) {
        return UsuarioMinResponseDTO.builder()
                .nombreUsuario(usuario.getNombreUsuario())
                .imagenPerfil(usuario.getImagenPerfil())
                .build();
    }
}
