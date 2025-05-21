package com.cinelog.cinelog_backend.service;

import com.cinelog.cinelog_backend.dto.request.UsuarioPasswordUpdateRequestDTO;
import com.cinelog.cinelog_backend.dto.request.UsuarioRegisterRequestDTO;
import com.cinelog.cinelog_backend.dto.request.UsuarioRequestDTO;
import com.cinelog.cinelog_backend.dto.request.UsuarioUpdateRequestDTO;
import com.cinelog.cinelog_backend.dto.response.UsuarioRegisterResponseDTO;
import com.cinelog.cinelog_backend.dto.response.UsuarioResponseDTO;
import com.cinelog.cinelog_backend.exception.UsuarioYaExisteException;
import com.cinelog.cinelog_backend.mapper.UsuarioMapper;
import com.cinelog.cinelog_backend.model.Usuario;
import com.cinelog.cinelog_backend.repository.EntradaDiarioRepository;
import com.cinelog.cinelog_backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.cinelog.cinelog_backend.exception.TokenInvalidoException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EntradaDiarioRepository entradaDiarioRepository;


    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService,
                          EntradaDiarioRepository entradaDiarioRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.entradaDiarioRepository = entradaDiarioRepository;

    }

    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {
        Usuario usuario = UsuarioMapper.toEntity(dto);
        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponseDTO(guardado);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<UsuarioResponseDTO> findById(Long id) {
        return usuarioRepository.findById(id).map(UsuarioMapper::toResponseDTO);
    }

    public UsuarioRegisterResponseDTO registrarUsuarioConConfirmacion(UsuarioRegisterRequestDTO dto) {

        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UsuarioYaExisteException("El email ya está registrado.");
        }


        if (usuarioRepository.findByNombreUsuario(dto.getNombreUsuario()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        if (!dto.getContraseña().equals(dto.getRepetirContraseña())) {
            throw new RuntimeException("Las contraseñas no coinciden.");
        }

        String contraseñaHasheada = passwordEncoder.encode(dto.getContraseña());
        String token = UUID.randomUUID().toString();
        System.out.println("🔐 Token generado: " + token);


        Usuario usuario = UsuarioMapper.fromRegisterDTO(dto, contraseñaHasheada, token);
        usuarioRepository.save(usuario);

        // ✅ Aquí se envía el correo correctamente
        emailService.enviarCorreoActivacion(dto.getEmail(), dto.getNombre(), token);

        return UsuarioRegisterResponseDTO.builder()
                .mensaje("Registro exitoso. Revisa tu correo para activar tu cuenta.")
                .build();
    }

    public UsuarioRegisterResponseDTO activarCuenta(String token) {
        System.out.println("📨 Token recibido por activación: " + token);

        Optional<Usuario> posibleUsuario = usuarioRepository.findByTokenActivacion(token);

        if (posibleUsuario.isEmpty()) {
            // ⚠️ Podría ya estar activado
            Optional<Usuario> yaActivado = usuarioRepository.findAll().stream()
                    .filter(u -> u.getTokenActivacion() == null && u.isActivo())
                    .findFirst();

            if (yaActivado.isPresent()) {
                return UsuarioRegisterResponseDTO.builder()
                        .mensaje("La cuenta ya ha sido activada anteriormente. Puedes iniciar sesión.")
                        .build();
            }

            throw new TokenInvalidoException();
        }

        Usuario usuario = posibleUsuario.get();
        usuario.setActivo(true);
        usuario.setTokenActivacion(null);
        usuarioRepository.save(usuario);

        return UsuarioRegisterResponseDTO.builder()
                .mensaje("Cuenta activada correctamente. Ya puedes iniciar sesión.")
                .build();
    }
    public UsuarioResponseDTO actualizarDatos(Usuario actual, UsuarioUpdateRequestDTO nuevosDatos) {
        // Validar si el nombre de usuario ya existe y pertenece a otro usuario
        if (nuevosDatos.getNombreUsuario() != null &&
                !nuevosDatos.getNombreUsuario().equals(actual.getNombreUsuario())) {

            boolean yaExiste = usuarioRepository
                    .findByNombreUsuario(nuevosDatos.getNombreUsuario())
                    .filter(u -> !u.getId().equals(actual.getId()))
                    .isPresent();

            if (yaExiste) {
                throw new RuntimeException("El nombre de usuario ya está en uso.");
            }
        }

        // Aplicar cambios con el mapper
        UsuarioMapper.updateEntityFromDTO(actual, nuevosDatos);
        usuarioRepository.save(actual);
        return UsuarioMapper.toResponseDTO(actual);
    }

    public void cambiarContraseña(Usuario usuario, UsuarioPasswordUpdateRequestDTO dto) {
        // Validar contraseña actual
        if (!passwordEncoder.matches(dto.getContraseñaActual(), usuario.getContraseña())) {
            throw new RuntimeException("La contraseña actual no es válida.");
        }

        // Cambiar la contraseña
        String nuevaHash = passwordEncoder.encode(dto.getNuevaContraseña());
        usuario.setContraseña(nuevaHash);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminarCuenta(Usuario usuario) {
        // Primero eliminamos entradas relacionadas
        entradaDiarioRepository.deleteAllByUsuario(usuario);

        // Luego eliminamos el usuario
        usuarioRepository.deleteById(usuario.getId());
    }







}
