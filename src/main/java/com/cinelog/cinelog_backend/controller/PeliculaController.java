package com.cinelog.cinelog_backend.controller;

import com.cinelog.cinelog_backend.dto.response.PeliculaBusquedaResponseDTO;
import com.cinelog.cinelog_backend.dto.response.PeliculaResponseDTO;
import com.cinelog.cinelog_backend.mapper.PeliculaMapper;
import com.cinelog.cinelog_backend.model.Pelicula;
import com.cinelog.cinelog_backend.service.TmdbService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {

    private final TmdbService tmdbService;

    public PeliculaController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/buscar")
    public List<PeliculaBusquedaResponseDTO> buscarPeliculas(
            @RequestParam String query,
            @RequestParam(required = false) Integer año
    ) {
        return tmdbService.buscarPeliculas(query, año);
    }

    @GetMapping("/agregar")
    public PeliculaResponseDTO agregarPelicula(@RequestParam int id) {
        Pelicula pelicula = tmdbService.guardarSiNoExiste(id);
        return PeliculaMapper.toDTO(pelicula);
    }

}
