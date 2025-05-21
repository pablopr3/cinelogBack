package com.cinelog.cinelog_backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String nombreUsuario;
    private String email;
    private String imagenPerfil;
}