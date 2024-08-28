package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Usuario;

import br.org.unicortes.barbearia.repositories.UsuarioRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
            return new String[]{"CLIENT"};
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
                .withIssuer("Home")
                .withSubject(usuario.getEmail())
                .withClaim("id", usuario.getId())
                .withExpiresAt(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00")))
                .sign(Algorithm.HMAC256("secreta"));
    }
}
