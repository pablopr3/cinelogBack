package com.cinelog.cinelog_backend.security;

import com.cinelog.cinelog_backend.model.Usuario;
import com.cinelog.cinelog_backend.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // ✅ AÑADE ESTE IMPORT
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UsuarioRepository usuarioRepository) {
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("🚫 No se encontró el header Authorization o no empieza con 'Bearer'");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String email;

        try {
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            System.out.println("❌ Error al extraer el email del token: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("📨 Email extraído del token: " + email);

        // Solo si aún no hay autenticación establecida
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            usuarioRepository.findByEmail(email).ifPresentOrElse(usuario -> {
                System.out.println("✅ Usuario encontrado en base de datos: " + usuario.getEmail());

                // 👉 AÑADIMOS un rol explícito para que Spring permita acceder a rutas protegidas
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );


                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("🔐 Usuario autenticado correctamente en el contexto");

            }, () -> {
                System.out.println("❌ Usuario NO encontrado en la base con email: " + email);
            });
        }

        filterChain.doFilter(request, response);
    }
}
