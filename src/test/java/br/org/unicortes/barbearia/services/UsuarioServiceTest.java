package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.enums.Roles;
import br.org.unicortes.barbearia.exceptions.ConflitoException;
import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.*;
import br.org.unicortes.barbearia.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void deveListarTodosUsuarios() {
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.listarTodos();

        assertEquals(2, resultado.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void deveBuscarUsuarioPorId() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoAoBuscarUsuarioInexistentePorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> usuarioService.buscarPorId(1L));
    }

    @Test
    void deveCriarUsuarioComRoleCliente() {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail("teste@email.com");
        novoUsuario.setPassword("123");
        novoUsuario.setRole(Roles.CLIENT);

        when(usuarioRepository.existsByEmail(novoUsuario.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("hashed123");
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioService.criar(novoUsuario);

        assertEquals("hashed123", resultado.getPassword());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void naoDeveCriarUsuarioComEmailExistente() {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail("existente@email.com");

        when(usuarioRepository.existsByEmail("existente@email.com")).thenReturn(true);

        assertThrows(ConflitoException.class, () -> usuarioService.criar(novoUsuario));
    }

    @Test
    void deveAtualizarUsuario() {
        Usuario existente = Usuario.builder()
                .id(1L)
                .name("Fulano")
                .email("fulano@email.com")
                .password("123")
                .role(Roles.CLIENT)
                .ativo(true)
                .build();

        Usuario atualizado = Usuario.builder()
                .name("Ciclano")
                .email("novo@email.com")
                .password("novaSenha")
                .role(Roles.BARBER)
                .ativo(true)
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByEmailAndIdNot("novo@email.com", 1L)).thenReturn(false);
        when(passwordEncoder.encode("novaSenha")).thenReturn("hashedNovaSenha");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = usuarioService.atualizar(1L, atualizado);

        assertEquals("Ciclano", resultado.getName());
        assertEquals("novo@email.com", resultado.getEmail());
        assertEquals("hashedNovaSenha", resultado.getPassword());
        assertEquals(Roles.BARBER, resultado.getRole());
    }

    @Test
    void deveRemoverUsuarioExistente() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.remover(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void deveBuscarUsuarioPorEmail() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@email.com");

        when(usuarioRepository.findByEmail("email@email.com")).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorEmail("email@email.com");

        assertEquals("email@email.com", resultado.getEmail());
    }

    @Test
    void deveAlterarSenhaDoUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setPassword("antiga");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("nova")).thenReturn("novaCodificada");

        usuarioService.alterarSenha(1L, "nova");

        verify(usuarioRepository).save(argThat(u -> u.getPassword().equals("novaCodificada")));
    }

    @Test
    void deveVerificarEmailDisponivel() {
        when(usuarioRepository.existsByEmail("livre@email.com")).thenReturn(false);
        assertTrue(usuarioService.verificarDisponibilidadeEmail("livre@email.com"));
    }

    @Test
    void deveAtivarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setAtivo(false);
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.ativarUsuario(1L);

        assertTrue(usuario.getAtivo());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveDesativarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setAtivo(true);
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.desativarUsuario(1L);

        assertFalse(usuario.getAtivo());
        verify(usuarioRepository).save(usuario);
    }
}
