package com.cinelog.cinelog_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequestDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String contrasena;
}
