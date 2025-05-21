package com.cinelog.cinelog_backend.config.seeder;

import com.cinelog.cinelog_backend.model.Usuario;
import com.cinelog.cinelog_backend.repository.UsuarioRepository;
import com.cinelog.cinelog_backend.security.JwtUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class UsuarioSeeder {

    @Bean
    @Order(1)
    public CommandLineRunner seedUsuarios(UsuarioRepository usuarioRepository,
                                          PasswordEncoder passwordEncoder,
                                          JwtUtil jwtUtil) {
        return args -> {
            List<Usuario> usuarios = List.of(
                    Usuario.builder()
                            .nombre("Usuario Demo")
                            .nombreUsuario("cinelogdemo")
                            .email("demo@cinelog.com")
                            .contrasena(passwordEncoder.encode("Demo1234"))
                            .activo(true)
                            .imagenPerfil("https://i.pravatar.cc/100?img=12")
                            .build(),
                    Usuario.builder()
                            .nombre("Ana Cinéfila")
                            .nombreUsuario("anacinema")
                            .email("ana@cinelog.com")
                            .contrasena(passwordEncoder.encode("Ana1234"))
                            .activo(true)
                            .imagenPerfil("https://i.pravatar.cc/100?img=5")
                            .build(),
                    Usuario.builder()
                            .nombre("Carlos Movie")
                            .nombreUsuario("carlosmovies")
                            .email("carlos@cinelog.com")
                            .contrasena(passwordEncoder.encode("Carlos1234"))
                            .activo(true)
                            .imagenPerfil("https://i.pravatar.cc/100?img=20")
                            .build()
            );

            for (Usuario usuario : usuarios) {
                if (usuarioRepository.findByEmail(usuario.getEmail()).isEmpty()) {
                    usuarioRepository.save(usuario);
                    System.out.println("✅ Usuario creado: " + usuario.getEmail() + " / " +
                            (usuario.getEmail().startsWith("demo") ? "Demo1234" :
                                    usuario.getNombre().split(" ")[0] + "1234"));

                    String token = jwtUtil.generateToken(usuario.getEmail());
                    System.out.println("🔐 TOKEN para " + usuario.getEmail() + ":");
                    System.out.println("Bearer " + token + "\n");
                }
            }
        };
    }
}
