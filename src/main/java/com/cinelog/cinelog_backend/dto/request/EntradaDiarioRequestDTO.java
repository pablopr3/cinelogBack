package com.cinelog.cinelog_backend.dto.request;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntradaDiarioRequestDTO {

    @NotNull
    private Long peliculaId;

    private boolean visto;

    private boolean favorito;

    @Min(1)
    @Max(10)
    private int puntuacion;

    @Size(max = 2000)
    private String comentario;
}
