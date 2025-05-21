package com.cinelog.cinelog_backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private String token;
    private String nombre;
    private String nombreUsuario;
    private String email;
}
