package com.cinelog.cinelog_backend.controller;

import com.cinelog.cinelog_backend.dto.request.EntradaDiarioRequestDTO;
import com.cinelog.cinelog_backend.dto.response.EntradaDiarioResponseDTO;
import com.cinelog.cinelog_backend.service.EntradaDiarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/api/diario")
@SecurityRequirement(name = "BearerAuth")
public class EntradaDiarioController {


    private final EntradaDiarioService entradaDiarioService;

    public EntradaDiarioController(EntradaDiarioService entradaDiarioService) {
        this.entradaDiarioService = entradaDiarioService;
    }

    @PostMapping("/entradas")
    public EntradaDiarioResponseDTO crearEntrada(@Valid @RequestBody EntradaDiarioRequestDTO dto) {
        return entradaDiarioService.crearEntrada(dto);
    }

    @GetMapping("/entradas")
    public List<EntradaDiarioResponseDTO> buscarEntradas(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) Integer puntuacionMin,
            @RequestParam(required = false) Integer puntuacionMax,
            @RequestParam(required = false, defaultValue = "false") boolean soloFavoritas,
            @RequestParam(required = false, defaultValue = "fechaResena_desc") String ordenarPor,
            @RequestParam(required = false, defaultValue = "true") boolean soloMias,
            @RequestParam(required = false) String titulo

    ) {
        return entradaDiarioService.buscarEntradas(
                genero, puntuacionMin, puntuacionMax, ordenarPor, soloMias, titulo, soloFavoritas
        );

    }

    @PutMapping("/entradas/{id}")
    public EntradaDiarioResponseDTO editarEntrada(
            @PathVariable Long id,
            @Valid @RequestBody EntradaDiarioRequestDTO dto
    ) {
        return entradaDiarioService.editarEntrada(id, dto);
    }

    @DeleteMapping("/entradas/{id}")
    public void eliminarEntrada(@PathVariable Long id) {
        entradaDiarioService.eliminarEntrada(id);
    }





}
