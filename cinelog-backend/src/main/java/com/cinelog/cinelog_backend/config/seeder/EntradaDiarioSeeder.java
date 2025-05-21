package com.cinelog.cinelog_backend.config.seeder;

import com.cinelog.cinelog_backend.model.*;
import com.cinelog.cinelog_backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.core.annotation.Order;

@Configuration
public class EntradaDiarioSeeder {

    @Bean
    @Order(3)
    public CommandLineRunner seedEntradasDeDiario(
            UsuarioRepository usuarioRepository,
            PeliculaRepository peliculaRepository,
            EntradaDiarioRepository diarioRepository
    ) {
        return args -> {
            List<Usuario> usuarios = usuarioRepository.findAll();
            List<Pelicula> peliculas = peliculaRepository.findAll();

            if (usuarios.size() < 3 || peliculas.size() < 5) {
                System.out.println("⚠️ No hay suficientes usuarios o películas para poblar el diario.");
                return;
            }

            Random random = new Random();
            String[] comentarios = {
                    "Me encantó esta película.",
                    "Bastante buena, la volvería a ver.",
                    "Una joya del cine.",
                    "No me convenció el final.",
                    "Excelente actuación.",
                    "Visualmente impresionante.",
                    "Historia muy floja.",
                    "Perfecta para una tarde de domingo.",
                    "Demasiado lenta.",
                    "Una obra maestra moderna.",
                    "La música me voló la cabeza.",
                    "Esperaba más.",
                    "Recomendadísima.",
                    "Ni fu ni fa.",
                    "Me dormí.",
                    "Qué película tan intensa.",
                    "Un clásico instantáneo.",
                    "Demasiado predecible.",
                    "Guion muy sólido.",
                    "Final inesperado y brillante."
            };

            int entradasCreadas = 0;

            for (Usuario usuario : usuarios) {
                for (int i = 0; i < 7 && entradasCreadas < 20; i++) {
                    Pelicula pelicula = peliculas.get(random.nextInt(peliculas.size()));

                    boolean yaExiste = diarioRepository.findByUsuarioIdAndPeliculaId(usuario.getId(), pelicula.getId()).isPresent();
                    if (yaExiste) continue;

                    EntradaDiario entrada = EntradaDiario.builder()
                            .usuario(usuario)
                            .pelicula(pelicula)
                            .fechaAgregado(LocalDateTime.now().minusDays(random.nextInt(100)))
                            .puntuacion(random.nextInt(6) + 5) // puntuación entre 5 y 10
                            .comentario(comentarios[entradasCreadas % comentarios.length])
                            .favorito(random.nextBoolean())
                            .visto(true)
                            .build();

                    diarioRepository.save(entrada);
                    entradasCreadas++;
                }
            }

            System.out.println("✅ Entradas de diario generadas: " + entradasCreadas);
        };
    }
}
