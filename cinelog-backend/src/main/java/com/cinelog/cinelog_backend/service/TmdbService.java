package com.cinelog.cinelog_backend.service;

import com.cinelog.cinelog_backend.dto.response.PeliculaBusquedaResponseDTO;
import com.cinelog.cinelog_backend.mapper.PeliculaMapper;
import com.cinelog.cinelog_backend.model.Genero;
import com.cinelog.cinelog_backend.model.Pelicula;
import com.cinelog.cinelog_backend.repository.GeneroRepository;
import com.cinelog.cinelog_backend.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private final PeliculaRepository peliculaRepository;
    private final GeneroRepository generoRepository;

    public TmdbService(PeliculaRepository peliculaRepository, GeneroRepository generoRepository) {
        this.peliculaRepository = peliculaRepository;
        this.generoRepository = generoRepository;
    }

    public List<PeliculaBusquedaResponseDTO> buscarPeliculas(String query, Integer año) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("query", query);

        if (año != null) {
            uriBuilder.queryParam("year", año);
        }

        String url = uriBuilder.toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> resultados = (List<Map<String, Object>>) response.get("results");

        return resultados.stream().map(p -> {
            Integer year = null;
            if (p.get("release_date") != null && !((String) p.get("release_date")).isEmpty()) {
                try {
                    year = LocalDate.parse((String) p.get("release_date")).getYear();
                } catch (Exception e) {
                    year = 0;
                }
            }
            return PeliculaBusquedaResponseDTO.builder()
                    .id((Integer) p.get("id"))
                    .titulo((String) p.get("title"))
                    .posterURL("https://image.tmdb.org/t/p/w500" + p.get("poster_path"))
                    .año(year != null ? year : 0)
                    .build();
        }).toList();
    }

    public Map<String, Object> obtenerDetallesPelicula(int idTmdb) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/" + idTmdb)
                .queryParam("api_key", apiKey)
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }

    public Pelicula guardarSiNoExiste(int idTmdb) {
        Optional<Pelicula> existente = peliculaRepository.findByIdTmdb(idTmdb);
        if (existente.isPresent()) {
            return existente.get();
        }

        Map<String, Object> datos = obtenerDetallesPelicula(idTmdb);

        Pelicula.PeliculaBuilder builder = Pelicula.builder()
                .idTmdb(idTmdb)
                .titulo((String) datos.get("title"))
                .posterURL("https://image.tmdb.org/t/p/w500" + datos.get("poster_path"))
                .sinopsis((String) datos.get("overview"))
                .puntuacionTMDB(((Number) datos.get("vote_average")).floatValue());

        if (datos.get("release_date") != null && !((String) datos.get("release_date")).isEmpty()) {
            builder.año(LocalDate.parse((String) datos.get("release_date")).getYear());
        } else {
            builder.año(0);
        }

        List<Map<String, Object>> generosTmdb = (List<Map<String, Object>>) datos.get("genres");
        List<Genero> generos = generosTmdb.stream()
                .map(g -> {
                    int idTmdbGenero = (Integer) g.get("id");
                    return generoRepository.findByIdTmdb(idTmdbGenero)
                            .orElseGet(() -> {
                                Genero nuevo = Genero.builder()
                                        .idTmdb(idTmdbGenero)
                                        .nombre((String) g.get("name"))
                                        .build();
                                return generoRepository.save(nuevo);
                            });
                }).toList();

        Pelicula pelicula = builder.generos(generos).build();
        return peliculaRepository.save(pelicula);
    }
}
