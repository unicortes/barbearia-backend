package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Usuario;

import br.org.unicortes.barbearia.repositories.UsuarioRepository;
import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String secretKey = "secreta";


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findByEmail(username);
        if (user != null) {
            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + username);
        }
    }


    private String[] getRoles(Usuario user) {
        if (user.getRole() == null){
            return new String[]{"ROLE_CLIENT"};
        }

        return user.getRole().split(",");
    }

    public Usuario createUser(Usuario user) {
        if (usuarioRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Usuário já existe");//Criar exceção especifica
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.usuarioRepository.save(user);
    }

    public Usuario getUsuarioByEmail(String email) {
        return this.usuarioRepository.findByEmail(email);
    }

   public String gerarToken(Usuario usuario){
        return JWT.create()
                .withSubject(usuario.getEmail())
                .withClaim("roles", List.of(usuario.getRole()))
                .withExpiresAt(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00")))
                .sign(Algorithm.HMAC256("secreta"));
    }

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    private DecodedJWT decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public String getUsernameFromToken(String token) {
        if (validateToken(token)) {
            DecodedJWT decodedJWT = decodeToken(token);
            return decodedJWT != null ? decodedJWT.getSubject() : null;
        }
        return null;
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        if (validateToken(token)) {
            DecodedJWT decodedJWT = decodeToken(token);
            if (decodedJWT != null) {
                List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
                if (roles == null) {
                    return Collections.emptyList();
                }
                return roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    public Usuario getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return this.getUsuarioByEmail(auth.getName());
    }
}
