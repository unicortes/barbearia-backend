package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.enums.Roles;
import br.org.unicortes.barbearia.exceptions.ConflitoException;
import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.repositories.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() throws Exception {
        var field = AuthService.class.getDeclaredField("secretKey");
        field.setAccessible(true);
        field.set(authService, "testsecret");
    }

    @Test
    void carregarUsuarioByEmail_UsuarioExiste() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setName("teste");
        usuario.setPassword("senha123");
        usuario.setRole(Roles.ADMIN);

        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = authService.loadUserByUsername("teste@email.com");

        assertEquals("teste@email.com", userDetails.getUsername());

        assertEquals("senha123", userDetails.getPassword());

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void carregarUsuarioByEmail_UsuarioNaoExiste() {
        when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> authService.loadUserByUsername("naoexiste@email.com"));
    }

    @Test
    void criarUsuarioNovo() {
        Usuario usuario = new Usuario();
        usuario.setEmail("novo@email.com");
        usuario.setPassword("1234");

        when(usuarioRepository.findByEmail("novo@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("senhaCodificada");
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario savedUser = authService.createUser(usuario);

        assertEquals("senhaCodificada", savedUser.getPassword());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void criarUsuarioExistente() {
        Usuario usuario = new Usuario();
        usuario.setEmail("existente@email.com");

        when(usuarioRepository.findByEmail("existente@email.com")).thenReturn(Optional.of(new Usuario()));

        assertThrows(ConflitoException.class, () -> authService.createUser(usuario));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void gerarToken_UsuarioValido() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@teste.com");
        usuario.setRole(Roles.CLIENT);

        String token = authService.gerarToken(usuario);

        assertNotNull(token);
        assertTrue(authService.validateToken(token));
        assertEquals("email@teste.com", authService.getUsernameFromToken(token));
    }

    @Test
    void ValidarTokenInvalido() {
        assertFalse(authService.validateToken("tokeninvalido"));
    }

    @Test
    void gerarTokenUsuarioAdm() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@teste.com");
        usuario.setRole(Roles.ADMIN);

        String token = authService.gerarToken(usuario);

        var authorities = authService.getAuthoritiesFromToken(token);

        assertFalse(authorities.isEmpty());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void obterUsuarioPrincipal_UsuarioNaoExiste() {
        SecurityContextHolder.clearContext();
        Optional<Usuario> principal = authService.getPrincipal();
        assertTrue(principal.isEmpty());
    }

    @Test
    void ObterUsuarioPrincipal_UsuarioExiste() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@teste.com");
        when(usuarioRepository.findByEmail("email@teste.com")).thenReturn(Optional.of(usuario));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("email@teste.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        Optional<Usuario> principal = authService.getPrincipal();

        assertTrue(principal.isPresent());
        assertEquals("email@teste.com", principal.get().getEmail());
    }
}
