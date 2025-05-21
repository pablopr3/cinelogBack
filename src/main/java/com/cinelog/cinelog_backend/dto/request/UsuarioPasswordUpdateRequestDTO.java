package com.cinelog.cinelog_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPasswordUpdateRequestDTO {

    @NotBlank
    private String contrasenaActual;

    @NotBlank
    private String nuevacontrasena;
}
