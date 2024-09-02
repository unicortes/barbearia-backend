package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.exceptions.UsernameNotFoundException;
import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Mockito.reset(authService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void registerUserSuccessful() throws Exception {
        Usuario newUser = new Usuario();
        newUser.setEmail("newuser@exemplo.com");
        newUser.setRole("ADMIN");
        newUser.setPassword("password");

        Mockito.when(authService.createUser(Mockito.any(Usuario.class))).thenReturn(newUser);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newuser@exemplo.com"));
    }

    @Test
    void loginUserWithValidToken() throws Exception {
        Usuario user = new Usuario();
        user.setEmail("test@exemplo.com");
        user.setPassword("password");
        user.setToken("fake-jwt-token");

        Mockito.when(authService.loadUserByUsername("test@exemplo.com")).thenReturn(user);
        Mockito.when(authService.getUsuarioByEmail("test@exemplo.com")).thenReturn(user);
        Mockito.when(authService.gerarToken(Mockito.any(Usuario.class))).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@exemplo.com"))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void registerUserWithExistingEmail() throws Exception {
        Usuario existingUser = new Usuario();
        existingUser.setEmail("existing@exemplo.com");
        existingUser.setPassword("password");

        Mockito.when(authService.createUser(Mockito.any(Usuario.class)))
                .thenThrow(new RuntimeException("Usuário já existe"));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingUser)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Usuário já existe"));
    }

    @Test
    void loginUserWithInvalidCredentials() throws Exception {
        Usuario invalidUser = new Usuario();
        invalidUser.setEmail("invalid@exemplo.com");
        invalidUser.setPassword("wrongpassword");

        Mockito.when(authService.loadUserByUsername("invalid@exemplo.com"))
                .thenThrow(new UsernameNotFoundException("Usuário não encontrado"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Usuário não encontrado"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void logoutUserSuccessfully() throws Exception {
        // Supondo que exista um endpoint /logout no AuthController
        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void loginUserWithEmptyCredentials() throws Exception {
        Usuario emptyUser = new Usuario();
        emptyUser.setEmail("");
        emptyUser.setPassword("");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void registerUserWithoutAdminRole() throws Exception {
        Usuario newUser = new Usuario();
        newUser.setEmail("newuser@exemplo.com");
        newUser.setRole("CLIENT");
        newUser.setPassword("password");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isForbidden());
    }

    @Test
    void loginUserWithInvalidToken() throws Exception {
        Usuario user = new Usuario();
        user.setEmail("test@exemplo.com");
        user.setPassword("password");
        user.setToken("invalid-jwt-token");

        Mockito.when(authService.loadUserByUsername("test@exemplo.com")).thenReturn(user);
        Mockito.when(authService.getUsuarioByEmail("test@exemplo.com")).thenReturn(user);
        Mockito.when(authService.gerarToken(Mockito.any(Usuario.class))).thenReturn("invalid-jwt-token");
        Mockito.when(authService.validateToken("invalid-jwt-token")).thenReturn(false);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token inválido"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void registerUserWithEncodedPassword() throws Exception {
        Usuario newUser = new Usuario();
        newUser.setEmail("newuser@exemplo.com");
        newUser.setEmail("CLIENT");
        newUser.setPassword("plainpassword");

        Mockito.when(authService.createUser(Mockito.any(Usuario.class))).thenAnswer(invocation -> {
            Usuario user = invocation.getArgument(0);
            user.setPassword("encodedpassword");  // Simulate password encoding
            return user;
        });

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").value("encodedpassword"));
    }
}
