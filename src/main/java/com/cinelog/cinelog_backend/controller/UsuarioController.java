package com.cinelog.cinelog_backend.controller;

import com.cinelog.cinelog_backend.dto.request.UsuarioPasswordUpdateRequestDTO;
import com.cinelog.cinelog_backend.dto.request.UsuarioRegisterRequestDTO;
import com.cinelog.cinelog_backend.dto.request.UsuarioRequestDTO;
import com.cinelog.cinelog_backend.dto.request.UsuarioUpdateRequestDTO;
import com.cinelog.cinelog_backend.dto.response.UsuarioRegisterResponseDTO;
import com.cinelog.cinelog_backend.dto.response.UsuarioResponseDTO;
import com.cinelog.cinelog_backend.model.Usuario;
import com.cinelog.cinelog_backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public UsuarioResponseDTO registrar(@Valid @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.crearUsuario(dto);
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO obtenerPorId(@PathVariable Long id) {
        return usuarioService.findById(id).orElse(null);
    }

    @PostMapping("/registro")
    public UsuarioRegisterResponseDTO registrarConConfirmacion(
            @Valid @RequestBody UsuarioRegisterRequestDTO dto
    ) {
        return usuarioService.registrarUsuarioConConfirmacion(dto);
    }

    @GetMapping("/activar")
    public UsuarioRegisterResponseDTO activarCuenta(@RequestParam("token") String token) {
        return usuarioService.activarCuenta(token);
    }
    @PutMapping("/actualizar")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @RequestBody UsuarioUpdateRequestDTO dto
    ) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(usuarioService.actualizarDatos(usuario, dto));
    }

    @PutMapping("/cambiar-contrasena")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<String> cambiarcontrasena(
            @RequestBody @Valid UsuarioPasswordUpdateRequestDTO dto
    ) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        usuarioService.cambiarcontrasena(usuario, dto);
        return ResponseEntity.ok("contrasena actualizada correctamente.");
    }

    @DeleteMapping("/eliminar")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<String> eliminarCuenta() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        usuarioService.eliminarCuenta(usuario);
        return ResponseEntity.ok("Cuenta eliminada correctamente.");
    }




}
