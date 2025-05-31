package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.*;
import br.org.unicortes.barbearia.models.*;
import br.org.unicortes.barbearia.repositories.*;
import br.org.unicortes.barbearia.services.interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado"));
    }

    @Override
    @Transactional
    public Usuario criar(Usuario novoUsuario) {
        if (usuarioRepository.existsByEmail(novoUsuario.getEmail())) {
            throw new ConflitoException("Email já está em uso por outro usuário");
        }

        String senhaCodificada = passwordEncoder.encode(novoUsuario.getPassword());
        novoUsuario.setPassword(senhaCodificada);

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        switch (usuarioSalvo.getRole()) {
            case BARBER -> {
                Barbeiro barbeiro = new Barbeiro();
                barbeiro.setUsuario(usuarioSalvo);
                barbeiroRepository.save(barbeiro);
            }
            case CLIENT -> {
                Cliente cliente = new Cliente();
                cliente.setUsuario(usuarioSalvo);
                clienteRepository.save(cliente);
            }
        }

        return usuarioSalvo;
    }

    @Override
    @Transactional
    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado"));

        atualizarDadosUsuario(usuarioExistente, usuarioAtualizado);
        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado"));
    }

    @Override
    @Transactional
    public void alterarSenha(Long id, String novaSenha) {
        Usuario usuario = buscarPorId(id);
        usuario.setPassword(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void ativarUsuario(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void desativarUsuario(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarDisponibilidadeEmail(String email) {
        return !usuarioRepository.existsByEmail(email);
    }

    private void atualizarDadosUsuario(Usuario existente, Usuario atualizado) {
        if (atualizado.getEmail() != null) {
            validarEmailUnico(atualizado.getEmail(), existente.getId());
            existente.setEmail(atualizado.getEmail());
        }

        if (atualizado.getName() != null) {
            existente.setName(atualizado.getName());
        }

        if (atualizado.getRole() != null) {
            existente.setRole(atualizado.getRole());
        }

        if (atualizado.getPassword() != null) {
            existente.setPassword(passwordEncoder.encode(atualizado.getPassword()));
        }
    }

    private void validarEmailUnico(String email, Long idIgnorado) {
        if (usuarioRepository.existsByEmailAndIdNot(email, idIgnorado)) {
            throw new ConflitoException("Email já está em uso por outro usuário");
        }
    }
}
