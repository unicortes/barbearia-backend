package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.CartaoFidelidadeDTO;
import br.org.unicortes.barbearia.mappers.ICartaoFidelidadeMapper;
import br.org.unicortes.barbearia.models.CartaoFidelidade;
import br.org.unicortes.barbearia.models.Cliente;
import br.org.unicortes.barbearia.services.interfaces.ICartaoFidelidadeService;
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
class CartaoFidelidadeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ICartaoFidelidadeService cartaoFidelidadeService;

    @MockBean
    private ICartaoFidelidadeMapper cartaoFidelidadeMapper;

    private CartaoFidelidade cartaoFidelidade;
    private CartaoFidelidadeDTO cartaoFidelidadeDTO;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(10L);
        cliente.setName("Cliente Fiel");

        cartaoFidelidade = new CartaoFidelidade();
        cartaoFidelidade.setId(1L);
        cartaoFidelidade.setCliente(cliente);
        cartaoFidelidade.setPontos(10);

        cartaoFidelidadeDTO = new CartaoFidelidadeDTO();
        cartaoFidelidadeDTO.setId(1L);
        cartaoFidelidadeDTO.setClienteId(cliente.getId());
        cartaoFidelidadeDTO.setPontos(10);
    }

    @Test
    @DisplayName("Deve listar todos os cartões com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveListarTodosOsCartoesComSucesso() throws Exception {
        when(cartaoFidelidadeService.listarTodos()).thenReturn(Collections.singletonList(cartaoFidelidade));
        when(cartaoFidelidadeMapper.toDTO(any(CartaoFidelidade.class))).thenReturn(cartaoFidelidadeDTO);

        mockMvc.perform(get("/api/loyalty-cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)));
    }

    @Test
    @DisplayName("Deve buscar um cartão por ID com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveBuscarCartaoPorIdComSucesso() throws Exception {
        when(cartaoFidelidadeService.buscarPorId(1L)).thenReturn(cartaoFidelidade);
        when(cartaoFidelidadeMapper.toDTO(cartaoFidelidade)).thenReturn(cartaoFidelidadeDTO);

        mockMvc.perform(get("/api/loyalty-cards/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.pontos", is(10)));
    }

    @Test
    @DisplayName("Deve criar um novo cartão com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveCriarCartaoComSucesso() throws Exception {
        when(cartaoFidelidadeMapper.toEntity(any(CartaoFidelidadeDTO.class))).thenReturn(cartaoFidelidade);
        when(cartaoFidelidadeService.criar(any(CartaoFidelidade.class))).thenReturn(cartaoFidelidade);
        when(cartaoFidelidadeMapper.toDTO(any(CartaoFidelidade.class))).thenReturn(cartaoFidelidadeDTO);

        mockMvc.perform(post("/api/loyalty-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartaoFidelidadeDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/loyalty-cards/1"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Cartão de fidelidade criado com sucesso")));
    }

    @Test
    @DisplayName("Deve atualizar os pontos de um cartão com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarPontosComSucesso() throws Exception {
        int novosPontos = 25;
        cartaoFidelidade.setPontos(novosPontos);
        cartaoFidelidadeDTO.setPontos(novosPontos);

        when(cartaoFidelidadeService.atualizarPontos(eq(1L), eq(novosPontos))).thenReturn(cartaoFidelidade);
        when(cartaoFidelidadeMapper.toDTO(any(CartaoFidelidade.class))).thenReturn(cartaoFidelidadeDTO);

        mockMvc.perform(put("/api/loyalty-cards/{id}/pontos", 1L)
                        .param("pontos", String.valueOf(novosPontos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.pontos", is(novosPontos)))
                .andExpect(jsonPath("$.message", is("Pontos atualizados com sucesso")));
    }

    @Test
    @DisplayName("Deve remover um cartão com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveRemoverCartaoComSucesso() throws Exception {
        doNothing().when(cartaoFidelidadeService).remover(1L);

        mockMvc.perform(delete("/api/loyalty-cards/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Cartão de fidelidade removido com sucesso")));
    }
}
