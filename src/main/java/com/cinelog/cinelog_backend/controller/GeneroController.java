package com.cinelog.cinelog_backend.controller;

import com.cinelog.cinelog_backend.model.Genero;
import com.cinelog.cinelog_backend.repository.GeneroRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generos")
public class GeneroController {

    private final GeneroRepository generoRepository;

    public GeneroController(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    @GetMapping
    public List<String> listarGeneros() {
        return generoRepository.findAll()
                .stream()
                .map(Genero::getNombre)
                .sorted()
                .toList();
    }
}
