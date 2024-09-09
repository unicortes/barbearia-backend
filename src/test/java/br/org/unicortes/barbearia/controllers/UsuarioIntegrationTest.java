package br.org.unicortes.barbearia.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import br.org.unicortes.barbearia.controllers.UsuarioController;
import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.services.UsuarioService;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsuarios() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setName("John Doe");
        usuario1.setEmail("john@example.com");
        usuario1.setPassword("password");
        usuario1.setRole("ADMIN");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setName("Jane Smith");
        usuario2.setEmail("jane@example.com");
        usuario2.setPassword("password");
        usuario2.setRole("USER");

        List<Usuario> usuariosMock = List.of(usuario1, usuario2);

        when(usuarioService.getAllUsuarios()).thenReturn(usuariosMock);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUsuarioById() throws Exception {
        Long userId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(userId);
        usuario.setName("John Doe");
        usuario.setEmail("john@example.com");
        usuario.setPassword("password");
        usuario.setRole("ADMIN");

        when(usuarioService.getUsuarioById(userId)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateUsuario() throws Exception {
        Long userId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(userId);
        usuario.setName("Updated User");
        usuario.setEmail("updated@example.com");
        usuario.setPassword("newPassword");
        usuario.setRole("ADMIN");

        when(usuarioService.updateUsuario(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUsuario() throws Exception {
        Long userId = 1L;

        doNothing().when(usuarioService).deleteUsuario(userId);

        mockMvc.perform(delete("/api/usuarios/{id}", userId))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).deleteUsuario(userId);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAccessDeniedForNonAdmin() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/usuarios/{id}", 1L))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/usuarios/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Usuario())))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/usuarios/{id}", 1L))
                .andExpect(status().isForbidden());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
