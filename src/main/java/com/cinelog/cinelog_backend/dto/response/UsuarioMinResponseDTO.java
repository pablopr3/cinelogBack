package com.cinelog.cinelog_backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioMinResponseDTO {
    private String nombreUsuario;
    private String imagenPerfil;
}
