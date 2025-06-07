package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.enums.Roles;
import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.repositories.UsuarioRepository;
import br.org.unicortes.barbearia.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
        Usuario admin = new Usuario();
        admin.setName("Admin Teste");
        admin.setEmail("admin@teste.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Roles.ADMIN);

        usuarioRepository.save(admin);

        adminToken = "Bearer " + authService.gerarToken(admin);
    }

    @Test
    void deveRegistrarUsuarioComSucesso() throws Exception {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setName("Usuário Novo");
        novoUsuario.setEmail("novo@teste.com");
        novoUsuario.setPassword("123456");
        novoUsuario.setRole(Roles.ADMIN);

        mockMvc.perform(post("/auth/register")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Usuário criado com sucesso"))
                .andExpect(jsonPath("$.data.email").value("novo@teste.com"));
    }

    @Test
    void deveNegarAcessoAoRegistrarUsuarioSemToken() throws Exception {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setName("Usuário Novo");
        novoUsuario.setEmail("novo@teste.com");
        novoUsuario.setPassword("123456");
        novoUsuario.setRole(Roles.CLIENT);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoUsuario)))
                .andExpect(status().isForbidden());
    }

    @Test
    void devePermitirAcessoAoEndpointAdminComTokenValido() throws Exception {
        mockMvc.perform(get("/auth/admin/test")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Admin access granted!"));
    }

    @Test
    void deveNegarAcessoAoEndpointAdminSemToken() throws Exception {
        mockMvc.perform(get("/auth/admin/test"))
                .andExpect(status().isForbidden());
    }
}
