package br.org.unicortes.barbearia.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.repositories.UsuarioRepository;
import br.org.unicortes.barbearia.repositories.BarberRepository;
import br.org.unicortes.barbearia.repositories.ClientRepository;
import br.org.unicortes.barbearia.services.UsuarioService;
import br.org.unicortes.barbearia.exceptions.UsernameNotFoundException;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BarberRepository barberRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsuarios() {
        
        List<Usuario> usuariosMock = List.of(new Usuario(), new Usuario());
        when(usuarioRepository.findAll()).thenReturn(usuariosMock);

        
        List<Usuario> usuarios = usuarioService.getAllUsuarios();

       
        assertEquals(usuariosMock, usuarios);
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testGetUsuarioByIdThrowsException() {
        
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> usuarioService.getUsuarioById(id));
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateUsuario() {
        
        Usuario usuarioMock = new Usuario();
        usuarioMock.setName("existingUser");
        Usuario usuarioToUpdate = new Usuario();
        usuarioToUpdate.setName("newUser");
        usuarioToUpdate.setEmail("new@example.com");
        usuarioToUpdate.setPassword("newPassword");
        
        when(usuarioRepository.findByName("existingUser")).thenReturn(usuarioMock);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(usuarioRepository.save(usuarioMock)).thenReturn(usuarioMock);

        
        Usuario updatedUsuario = usuarioService.updateUsuario(usuarioToUpdate);

       
        assertEquals("newUser", updatedUsuario.getName());
        assertEquals("new@example.com", updatedUsuario.getEmail());
        assertEquals("encodedPassword", updatedUsuario.getPassword());
        verify(usuarioRepository, times(1)).save(usuarioMock);
    }

    @Test
    void testDeleteUsuarioAsBarber() {
        
        Long id = 1L;
        Usuario usuarioMock = new Usuario();
        usuarioMock.setRole("BARBER");
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioMock));

        
        usuarioService.deleteUsuario(id);

       
        verify(usuarioRepository, times(1)).delete(usuarioMock);
    }

    @Test
    void testDeleteUsuarioAsClient() {
        
        Long id = 2L;
        Usuario usuarioMock = new Usuario();
        usuarioMock.setRole("CLIENT");
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioMock));

        
        usuarioService.deleteUsuario(id);
        verify(usuarioRepository, times(1)).delete(usuarioMock);
    }
}

