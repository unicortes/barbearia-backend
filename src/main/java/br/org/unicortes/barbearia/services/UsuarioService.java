package br.org.unicortes.barbearia.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.org.unicortes.barbearia.exceptions.UsernameNotFoundException;
import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Long id) {
        return this.usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user"));
    }

    public Usuario updateUsuario(Usuario usuario) {
        Usuario usuarioAAtualizar = this.usuarioRepository.findByName(usuario.getName());

        usuarioAAtualizar.setEmail(usuario.getEmail());
        usuarioAAtualizar.setName(usuario.getName());
        usuarioAAtualizar.setRole(usuario.getRole());
        usuarioAAtualizar.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return this.usuarioRepository.save(usuarioAAtualizar);

    }

    public void deleteUsuario(Long id) {
        Usuario usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("user"));
        this.usuarioRepository.delete(usuario);
    }
}
