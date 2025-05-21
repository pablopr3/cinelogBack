package com.cinelog.cinelog_backend.service;

import com.cinelog.cinelog_backend.dto.request.AuthRequestDTO;
import com.cinelog.cinelog_backend.dto.request.OlvideContrasenaRequestDTO;
import com.cinelog.cinelog_backend.dto.request.RestablecerContrasenaRequestDTO;
import com.cinelog.cinelog_backend.dto.response.AuthResponseDTO;
import com.cinelog.cinelog_backend.model.Usuario;
import com.cinelog.cinelog_backend.repository.UsuarioRepository;
import com.cinelog.cinelog_backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    public AuthResponseDTO login(AuthRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email no registrado"));

        if (!usuario.isActivo()) {
            throw new RuntimeException("La cuenta no ha sido activada.");
        }

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException("contrasena incorrecta");
        }

        String token = jwtUtil.generateToken(usuario.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .nombre(usuario.getNombre())
                .nombreUsuario(usuario.getNombreUsuario())
                .email(usuario.getEmail())
                .build();
    }

    public void solicitarRecuperacion(OlvideContrasenaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("No existe una cuenta con ese correo."));

        String token = UUID.randomUUID().toString();
        Date expiracion = new Date(System.currentTimeMillis() + 30 * 60 * 1000); // 30 minutos

        usuario.setTokenRecuperacion(token);
        usuario.setTokenExpira(expiracion);
        usuarioRepository.save(usuario);

        emailService.enviarCorreoRecuperacion(usuario.getEmail(), usuario.getNombre(), token);
    }

    public ResponseEntity<String> restablecercontrasena(RestablecerContrasenaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByTokenRecuperacion(dto.getToken())
                .orElseThrow(() -> new RuntimeException("El enlace de recuperación es inválido o ya fue usado."));

        if (usuario.getTokenExpira() == null || usuario.getTokenExpira().before(new Date())) {
            return ResponseEntity.badRequest().body("El enlace ha caducado. Solicita uno nuevo.");
        }

        String contrasena = dto.getNuevacontrasena();

        if (contrasena.length() < 8) {
            return ResponseEntity.badRequest().body("La contrasena debe tener al menos 8 caracteres.");
        }
        if (!contrasena.matches(".*[A-Z].*")) {
            return ResponseEntity.badRequest().body("La contrasena debe contener al menos una letra mayúscula.");
        }
        if (!contrasena.matches(".*\\d.*")) {
            return ResponseEntity.badRequest().body("La contrasena debe contener al menos un número.");
        }

        String contrasenaHasheada = passwordEncoder.encode(contrasena);
        usuario.setContrasena(contrasenaHasheada);
        usuario.setTokenRecuperacion(null);
        usuario.setTokenExpira(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("contrasena restablecida correctamente.");
    }
}
