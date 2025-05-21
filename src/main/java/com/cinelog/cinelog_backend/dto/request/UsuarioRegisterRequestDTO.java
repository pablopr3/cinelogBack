package com.cinelog.cinelog_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRegisterRequestDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    @Size(min = 4, max = 20)
    private String nombreUsuario;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = ".*[A-Z].*", message = "Debe contener al menos una mayúscula")
    @Pattern(regexp = ".*\\d.*", message = "Debe contener al menos un número")
    private String contraseña;

    @NotBlank
    private String repetirContraseña;
}
