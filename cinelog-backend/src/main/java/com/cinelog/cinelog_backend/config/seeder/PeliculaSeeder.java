package com.cinelog.cinelog_backend.config.seeder;

import com.cinelog.cinelog_backend.model.Pelicula;
import com.cinelog.cinelog_backend.service.TmdbService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class PeliculaSeeder {

    @Bean
    @Order(2)
    public CommandLineRunner seedPeliculas(TmdbService tmdbService) {
        return args -> {
            List<Integer> idsTmdb = List.of(
                    157336, // Interstellar
                    550,    // Fight Club
                    680,    // Pulp Fiction
                    278,    // The Shawshank Redemption
                    238,    // The Godfather
                    424,    // Schindler's List
                    13,     // Forrest Gump
                    27205,  // Inception
                    497,    // The Green Mile
                    155     // The Dark Knight
            );

            int añadidas = 0;

            for (int idTmdb : idsTmdb) {
                Pelicula pelicula = tmdbService.guardarSiNoExiste(idTmdb);
                if (pelicula != null) {
                    System.out.println("🎬 Película guardada: " + pelicula.getTitulo());
                    añadidas++;
                }
            }

            System.out.println("✅ Total de películas agregadas: " + añadidas);
        };
    }
}
