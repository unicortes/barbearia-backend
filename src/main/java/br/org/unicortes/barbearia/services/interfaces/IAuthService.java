package br.org.unicortes.barbearia.services.interfaces;

import br.org.unicortes.barbearia.models.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IAuthService extends UserDetailsService {
    Usuario createUser(Usuario user);
    Optional<Usuario> getUsuarioByEmail(String email);
    String gerarToken(Usuario usuario);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    List<GrantedAuthority> getAuthoritiesFromToken(String token);
    Optional<Usuario> getPrincipal();
}
