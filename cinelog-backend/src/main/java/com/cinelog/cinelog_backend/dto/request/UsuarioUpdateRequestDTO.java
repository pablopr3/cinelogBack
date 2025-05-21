package com.cinelog.cinelog_backend.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioUpdateRequestDTO {
    private String nombre;
    private String nombreUsuario;
    private String imagenPerfil;
}
