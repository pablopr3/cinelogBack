package com.cinelog.cinelog_backend.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntradaDiarioResponseDTO {

    private Long id;
    private int puntuacion;
    private String comentario;
    private boolean favorito;
    private boolean visto;
    private LocalDateTime fechaAgregado;

    private PeliculaResponseDTO pelicula;
    private UsuarioMinResponseDTO usuario; // Solo nombre de usuario e imagen
}
