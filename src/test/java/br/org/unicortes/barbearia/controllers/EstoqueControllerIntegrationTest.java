package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.EstoqueDTO;
import br.org.unicortes.barbearia.mappers.IEstoqueMapper;
import br.org.unicortes.barbearia.models.Estoque;
import br.org.unicortes.barbearia.models.Produto;
import br.org.unicortes.barbearia.services.interfaces.IEstoqueService;
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

import java.math.BigDecimal;
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
class EstoqueControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IEstoqueService estoqueService;

    @MockBean
    private IEstoqueMapper estoqueMapper;

    private Estoque estoque;
    private EstoqueDTO estoqueDTO;
    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(10L);
        produto.setNome("Pomada Modeladora");
        produto.setPrecoCusto(new BigDecimal("45.50"));

        estoque = new Estoque();
        estoque.setId(1L);
        estoque.setProduto(produto);
        estoque.setQuantidade(50);

        estoqueDTO = new EstoqueDTO();
        estoqueDTO.setId(1L);
        estoqueDTO.setProduto(produto.getId());
        estoqueDTO.setQuantidade(50);
    }

    @Test
    @DisplayName("Deve listar todos os itens de estoque com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveListarTodosOsItensDeEstoqueComSucesso() throws Exception {
        when(estoqueService.listarTodos()).thenReturn(Collections.singletonList(estoque));
        when(estoqueMapper.toDTO(any(Estoque.class))).thenReturn(estoqueDTO);

        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)));
    }

    @Test
    @DisplayName("Deve buscar um item de estoque por ID com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveBuscarItemDeEstoquePorIdComSucesso() throws Exception {
        when(estoqueService.buscarPorId(1L)).thenReturn(estoque);
        when(estoqueMapper.toDTO(estoque)).thenReturn(estoqueDTO);

        mockMvc.perform(get("/api/stocks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.quantidade", is(50)));
    }

    @Test
    @DisplayName("Deve criar um novo item de estoque com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveCriarItemDeEstoqueComSucesso() throws Exception {
        when(estoqueMapper.toEntity(any(EstoqueDTO.class))).thenReturn(estoque);
        when(estoqueService.criar(any(Estoque.class))).thenReturn(estoque);
        when(estoqueMapper.toDTO(any(Estoque.class))).thenReturn(estoqueDTO);

        mockMvc.perform(post("/api/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estoqueDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/stocks/1"))
                .andExpect(jsonPath("$.message", is("Estoque criado com sucesso")));
    }

    @Test
    @DisplayName("Deve atualizar um item de estoque com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarItemDeEstoqueComSucesso() throws Exception {
        EstoqueDTO dtoAtualizado = new EstoqueDTO();
        dtoAtualizado.setQuantidade(40);

        Estoque estoqueAtualizado = new Estoque();
        estoqueAtualizado.setId(1L);
        estoqueAtualizado.setQuantidade(40);

        estoqueDTO.setQuantidade(40);

        when(estoqueMapper.toEntity(any(EstoqueDTO.class))).thenReturn(estoqueAtualizado);
        when(estoqueService.atualizar(eq(1L), any(Estoque.class))).thenReturn(estoqueAtualizado);
        when(estoqueMapper.toDTO(any(Estoque.class))).thenReturn(estoqueDTO);

        mockMvc.perform(put("/api/stocks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.quantidade", is(40)))
                .andExpect(jsonPath("$.message", is("Estoque atualizado com sucesso")));
    }

    @Test
    @DisplayName("Deve remover um item de estoque com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveRemoverItemDeEstoqueComSucesso() throws Exception {
        doNothing().when(estoqueService).remover(1L);

        mockMvc.perform(delete("/api/stocks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Estoque removido com sucesso")));
    }
}
