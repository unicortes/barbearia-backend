package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.repositories.UsuarioRepository;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByValidUser() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@exemplo.com");
        usuario.setPassword("senha123");
        usuario.setRole("ADMIN");
        when(usuarioRepository.findByEmail("teste@exemplo.com")).thenReturn(usuario);

        UserDetails userDetails = authService.loadUserByUsername("teste@exemplo.com");

        assertNotNull(userDetails);
        assertEquals("teste@exemplo.com", userDetails.getUsername());
        assertEquals("senha123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByInexistentUser() {
        when(usuarioRepository.findByEmail("inexistente@exemplo.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () ->
                authService.loadUserByUsername("inexistente@exemplo.com"));
    }

    @Test
    void createUserAlreadyExistent() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@exemplo.com");
        when(usuarioRepository.findByEmail("teste@exemplo.com")).thenReturn(usuario);

        assertThrows(RuntimeException.class, () ->
                authService.createUser(usuario));
    }

    @Test
    void gerarTokenValido() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@exemplo.com");
        usuario.setRole("ADMIN");

        String token = authService.gerarToken(usuario);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void invalidateToken() {
        String invalidToken = "invalid.token";

        boolean isValid = authService.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    void createUserSuccess() {
        Usuario usuario = new Usuario();
        usuario.setEmail("novo@exemplo.com");
        usuario.setPassword("senha123");
        usuario.setRole("ADMIN");

        when(usuarioRepository.findByEmail("novo@exemplo.com")).thenReturn(null);
        when(passwordEncoder.encode("senha123")).thenReturn("senha123_encoded");

        authService.createUser(usuario);

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void validarTokenValido() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@exemplo.com");
        usuario.setRole("ADMIN");

        String token = authService.gerarToken(usuario);

        boolean isValid = authService.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void loadUserByNullUsername() {
        assertThrows(UsernameNotFoundException.class, () ->
                authService.loadUserByUsername(null));
    }

    @Test
    void encodePasswordOnCreateUser() {
        Usuario usuario = new Usuario();
        usuario.setEmail("novo@exemplo.com");
        usuario.setPassword("senha123");
        usuario.setRole("ADMIN");

        when(usuarioRepository.findByEmail("novo@exemplo.com")).thenReturn(null);
        when(passwordEncoder.encode("senha123")).thenReturn("senha123_encoded");

        authService.createUser(usuario);

        verify(passwordEncoder, times(1)).encode("senha123");
        assertEquals("senha123_encoded", usuario.getPassword());
    }

    @Test
    void createUserNewUser() {
        Usuario usuario = new Usuario();
        usuario.setEmail("novo@exemplo.com");
        usuario.setPassword("senha123");
        usuario.setRole("ADMIN");
        when(usuarioRepository.findByEmail("novo@exemplo.com")).thenReturn(null);

        authService.createUser(usuario);

        verify(usuarioRepository, times(1)).save(usuario);
    }
}