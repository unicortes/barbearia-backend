package br.org.unicortes.barbearia.config.security;

import br.org.unicortes.barbearia.enums.Endpoints;
import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.services.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {
        try {
            extractTokenFromRequest(request).ifPresent(token -> {
                if (authService.validateToken(token)) {
                    String email = authService.getUsernameFromToken(token);
                    Optional<Usuario> usuario = authService.getUsuarioByEmail(email);

                    if (usuario.isPresent()) {
                        authenticateUser(request, usuario, token);
                        log.debug("Authenticated user: {}", email);
                    }
                }
            });

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authentication");
        }
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTH_HEADER))
                .filter(header -> header.startsWith(BEARER_PREFIX))
                .map(header -> header.substring(BEARER_PREFIX.length()))
                .filter(token -> !token.isBlank());
    }

    private void authenticateUser(HttpServletRequest request,
                                  Optional<Usuario> usuario,
                                  String token) {
        usuario.ifPresent(u -> {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            u,
                            null,
                            authService.getAuthoritiesFromToken(token)
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        });
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        String[] publicEndpoints = Endpoints.getPublicEndpoints();

        for (String pattern : publicEndpoints) {
            String cleanPattern = pattern.replace("/**", ""); // Remove wildcard para comparar prefixo
            if (path.startsWith(cleanPattern)) {
                return true; // Não filtra (libera acesso público)
            }
        }

        return false; // Filtra os demais (requer autenticação)
    }
}
