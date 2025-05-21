package com.cinelog.cinelog_backend.service;

import com.cinelog.cinelog_backend.dto.request.EntradaDiarioRequestDTO;
import com.cinelog.cinelog_backend.dto.response.EntradaDiarioResponseDTO;
import com.cinelog.cinelog_backend.mapper.EntradaDiarioMapper;
import com.cinelog.cinelog_backend.model.EntradaDiario;
import com.cinelog.cinelog_backend.model.Pelicula;
import com.cinelog.cinelog_backend.model.Usuario;
import com.cinelog.cinelog_backend.repository.EntradaDiarioRepository;
import com.cinelog.cinelog_backend.repository.PeliculaRepository;
import com.cinelog.cinelog_backend.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EntradaDiarioService {

    private final EntradaDiarioRepository diarioRepository;
    private final PeliculaRepository peliculaRepository;
    private final UsuarioRepository usuarioRepository;

    public EntradaDiarioService(EntradaDiarioRepository diarioRepository,
                                PeliculaRepository peliculaRepository,
                                UsuarioRepository usuarioRepository) {
        this.diarioRepository = diarioRepository;
        this.peliculaRepository = peliculaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public EntradaDiarioResponseDTO crearEntrada(EntradaDiarioRequestDTO dto) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pelicula pelicula = peliculaRepository.findById(dto.getPeliculaId())
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        boolean yaExiste = diarioRepository.findByUsuarioIdAndPeliculaId(usuario.getId(), pelicula.getId()).isPresent();
        if (yaExiste) {
            throw new RuntimeException("Ya tienes una reseña para esta película.");
        }

        EntradaDiario entrada = EntradaDiario.builder()
                .usuario(usuario)
                .pelicula(pelicula)
                .fechaAgregado(LocalDateTime.now())
                .puntuacion(dto.getPuntuacion())
                .comentario(dto.getComentario())
                .favorito(dto.isFavorito())
                .visto(dto.isVisto())
                .build();

        EntradaDiario guardada = diarioRepository.save(entrada);
        return EntradaDiarioMapper.toDTO(guardada);
    }

    public List<EntradaDiarioResponseDTO> buscarEntradas(
            String genero,
            Integer puntuacionMin,
            Integer puntuacionMax,
            String ordenarPor,
            boolean soloMias,
            String titulo,
            boolean soloFavoritas
    ) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<EntradaDiario> entradas = soloMias
                ? diarioRepository.findByUsuario(usuario)
                : diarioRepository.findAll();

        return entradas.stream()
                .filter(e -> genero == null || e.getPelicula().getGeneros().stream()
                        .anyMatch(g -> g.getNombre().equalsIgnoreCase(genero)))
                .filter(e -> puntuacionMin == null || e.getPuntuacion() >= puntuacionMin)
                .filter(e -> puntuacionMax == null || e.getPuntuacion() <= puntuacionMax)
                .filter(e -> !soloFavoritas || e.isFavorito())
                .filter(e -> titulo == null || e.getPelicula().getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .sorted((e1, e2) -> {
                    switch (ordenarPor.toLowerCase()) {
                        case "puntuacion_desc":
                            return Integer.compare(e2.getPuntuacion(), e1.getPuntuacion());
                        case "puntuacion_asc":
                            return Integer.compare(e1.getPuntuacion(), e2.getPuntuacion());
                        case "fecharesena_asc":
                            return e1.getFechaAgregado().compareTo(e2.getFechaAgregado());
                        case "fecharesena_desc":
                        default:
                            return e2.getFechaAgregado().compareTo(e1.getFechaAgregado());
                    }
                })
                .map(EntradaDiarioMapper::toDTO)
                .toList();
    }


    public EntradaDiarioResponseDTO editarEntrada(Long id, EntradaDiarioRequestDTO dto) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        EntradaDiario entrada = diarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada no encontrada"));

        if (!entrada.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permisos para editar esta entrada.");
        }

        entrada.setComentario(dto.getComentario());
        entrada.setPuntuacion(dto.getPuntuacion());
        entrada.setFavorito(dto.isFavorito());
        entrada.setVisto(dto.isVisto());

        return EntradaDiarioMapper.toDTO(diarioRepository.save(entrada));
    }

    public void eliminarEntrada(Long id) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        EntradaDiario entrada = diarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada no encontrada"));

        if (!entrada.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permisos para eliminar esta entrada.");
        }

        diarioRepository.delete(entrada);
    }
}
