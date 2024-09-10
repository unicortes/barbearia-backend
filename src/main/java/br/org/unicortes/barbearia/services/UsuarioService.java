package br.org.unicortes.barbearia.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.org.unicortes.barbearia.exceptions.UsernameNotFoundException;
import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.repositories.UsuarioRepository;
import br.org.unicortes.barbearia.repositories.BarberRepository;
import br.org.unicortes.barbearia.repositories.ClientRepository;
import br.org.unicortes.barbearia.models.Barber;
import br.org.unicortes.barbearia.models.Client;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BarberRepository barberRepository;

    @Autowired
    private ClientRepository clientRepository;

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

        if (usuarioAAtualizar == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com o nome: " + usuario.getName());
        }

        usuarioAAtualizar.setEmail(usuario.getEmail());
        usuarioAAtualizar.setName(usuario.getName());
        usuarioAAtualizar.setRole(usuario.getRole());
        usuarioAAtualizar.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return this.usuarioRepository.save(usuarioAAtualizar);
    }


    public void deleteUsuario(Long id) {
        Usuario usuario = this.usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user"));

        this.usuarioRepository.delete(usuario);
    }
}
