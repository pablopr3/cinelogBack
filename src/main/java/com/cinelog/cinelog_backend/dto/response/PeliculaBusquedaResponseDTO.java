package com.cinelog.cinelog_backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeliculaBusquedaResponseDTO {
    private int id;             // tiene que ser int porq la api usa int
    private String titulo;
    private String posterURL;
    private int año;
}
