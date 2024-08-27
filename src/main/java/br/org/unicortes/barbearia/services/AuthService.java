package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Usuario;

import br.org.unicortes.barbearia.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.usuarioRepository.save(user);
    }


}
