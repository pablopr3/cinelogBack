package com.cinelog.cinelog_backend.mapper;

import com.cinelog.cinelog_backend.dto.request.UsuarioRequestDTO;
import com.cinelog.cinelog_backend.dto.request.UsuarioRegisterRequestDTO;
import com.cinelog.cinelog_backend.dto.response.UsuarioResponseDTO;
import com.cinelog.cinelog_backend.dto.request.UsuarioUpdateRequestDTO;

import com.cinelog.cinelog_backend.model.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequestDTO dto) {
        return Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .contrasena(dto.getContrasena())
                .imagenPerfil(dto.getImagenPerfil())
                .build();
    }

    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .nombreUsuario(usuario.getNombreUsuario())
                .email(usuario.getEmail())
                .imagenPerfil(usuario.getImagenPerfil())
                .build();
    }

    public static Usuario fromRegisterDTO(UsuarioRegisterRequestDTO dto, String contrasenaHasheada, String tokenActivacion) {
        return Usuario.builder()
                .nombre(dto.getNombre())
                .nombreUsuario(dto.getNombreUsuario())
                .email(dto.getEmail())
                .contrasena(contrasenaHasheada)
                .activo(false)
                .tokenActivacion(tokenActivacion)
                .build();
    }
    public static void updateEntityFromDTO(Usuario usuario, UsuarioUpdateRequestDTO dto) {
        if (dto.getNombre() != null) {
            usuario.setNombre(dto.getNombre());
        }
        if (dto.getNombreUsuario() != null) {
            usuario.setNombreUsuario(dto.getNombreUsuario());
        }
        if (dto.getImagenPerfil() != null) {
            usuario.setImagenPerfil(dto.getImagenPerfil());
        }
    }
}
