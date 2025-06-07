package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.ClienteDTO;
import br.org.unicortes.barbearia.mappers.IClienteMapper;
import br.org.unicortes.barbearia.models.Cliente;
import br.org.unicortes.barbearia.services.interfaces.IClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IClienteService clienteService;

    @MockBean
    private IClienteMapper clienteMapper;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setName("Carlos Pereira");
        cliente.setEmail("carlos.pereira@email.com");
        cliente.setTelefone("11987654321");

        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setName("Carlos Pereira");
        clienteDTO.setEmail("carlos.pereira@email.com");
        clienteDTO.setTelefone("11987654321");
    }

    @Test
    @DisplayName("Deve listar todos os clientes com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveListarTodosOsClientesComSucesso() throws Exception {
        when(clienteService.listarTodos()).thenReturn(Collections.singletonList(cliente));
        when(clienteMapper.toDTO(cliente)).thenReturn(clienteDTO);

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].name", is("Carlos Pereira")));
    }

    @Test
    @DisplayName("Deve buscar um cliente por ID com sucesso")
    @WithMockUser
    void deveBuscarClientePorIdComSucesso() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(clienteMapper.toDTO(cliente)).thenReturn(clienteDTO);

        mockMvc.perform(get("/clientes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Carlos Pereira")));
    }

    @Test
    @DisplayName("Deve criar um novo cliente com sucesso")
    @WithMockUser
    void deveCriarClienteComSucesso() throws Exception {
        when(clienteMapper.toEntity(any(ClienteDTO.class))).thenReturn(cliente);
        when(clienteService.criar(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteDTO);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/clientes/1"))
                .andExpect(jsonPath("$.message", is("Cliente criado com sucesso")));
    }

    @Test
    @DisplayName("Deve atualizar um cliente com sucesso")
    @WithMockUser
    void deveAtualizarClienteComSucesso() throws Exception {
        ClienteDTO dtoAtualizado = new ClienteDTO();
        dtoAtualizado.setName("Carlos Pereira Atualizado");

        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setId(1L);
        clienteAtualizado.setName("Carlos Pereira Atualizado");

        when(clienteMapper.toEntity(any(ClienteDTO.class))).thenReturn(clienteAtualizado);
        when(clienteService.atualizar(eq(1L), any(Cliente.class))).thenReturn(clienteAtualizado);
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(dtoAtualizado);

        mockMvc.perform(put("/clientes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is("Carlos Pereira Atualizado")))
                .andExpect(jsonPath("$.message", is("Cliente atualizado com sucesso")));
    }

    @Test
    @DisplayName("Deve remover um cliente com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveRemoverClienteComSucesso() throws Exception {
        doNothing().when(clienteService).remover(1L);

        mockMvc.perform(delete("/clientes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Cliente removido com sucesso")));
    }

    @Test
    @DisplayName("Deve ativar um cliente com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveAtivarClienteComSucesso() throws Exception {
        doNothing().when(clienteService).ativarCliente(1L);

        mockMvc.perform(patch("/clientes/{id}/ativar", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Cliente ativado com sucesso")));
    }

    @Test
    @DisplayName("Deve desativar um cliente com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveDesativarClienteComSucesso() throws Exception {
        doNothing().when(clienteService).desativarCliente(1L);

        mockMvc.perform(patch("/clientes/{id}/desativar", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Cliente desativado com sucesso")));
    }
}
