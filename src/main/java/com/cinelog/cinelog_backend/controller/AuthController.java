package com.cinelog.cinelog_backend.controller;

import com.cinelog.cinelog_backend.dto.request.AuthRequestDTO;
import com.cinelog.cinelog_backend.dto.request.OlvideContraseñaRequestDTO;
import com.cinelog.cinelog_backend.dto.request.RestablecerContraseñaRequestDTO;
import com.cinelog.cinelog_backend.dto.response.AuthResponseDTO;
import com.cinelog.cinelog_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody AuthRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/olvide")
    public ResponseEntity<String> solicitarRecuperacion(
            @Valid @RequestBody OlvideContraseñaRequestDTO dto
    ) {
        authService.solicitarRecuperacion(dto);
        return ResponseEntity.ok("Se ha enviado un enlace para restablecer tu contraseña.");
    }

    @PostMapping("/restablecer")
    public ResponseEntity<String> restablecerContraseña(
            @Valid @RequestBody RestablecerContraseñaRequestDTO dto
    ) {
        return authService.restablecerContraseña(dto);
    }
}
