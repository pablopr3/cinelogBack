package com.cinelog.cinelog_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OlvideContraseñaRequestDTO {

    @Email
    @NotBlank
    private String email;
}