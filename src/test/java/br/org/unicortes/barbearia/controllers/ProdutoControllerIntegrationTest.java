package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.ProdutoDTO;
import br.org.unicortes.barbearia.mappers.IProdutoMapper;
import br.org.unicortes.barbearia.models.Produto;
import br.org.unicortes.barbearia.services.interfaces.IProdutoService;
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
class ProdutoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IProdutoService produtoService;

    @MockBean
    private IProdutoMapper produtoMapper;

    private Produto produto;
    private ProdutoDTO produtoDTO;

    @BeforeEach
    void setUp() {
        // Dados de exemplo para os testes
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Cera Modeladora Efeito Seco");
        produto.setDescricao("Cera para cabelos masculinos com alta fixação e efeito matte.");
        produto.setPrecoCusto(new BigDecimal("55.90"));
        produto.setAtivo(true);

        produtoDTO = new ProdutoDTO();
        produtoDTO.setId(1L);
        produtoDTO.setNome("Cera Modeladora Efeito Seco");
        produtoDTO.setDescricao("Cera para cabelos masculinos com alta fixação e efeito matte.");
        produtoDTO.setPrecoCusto(new BigDecimal("55.90"));
        produtoDTO.setAtivo(true);
    }

    @Test
    @DisplayName("Deve listar todos os produtos com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveListarTodosOsProdutosComSucesso() throws Exception {
        when(produtoService.listarTodos()).thenReturn(Collections.singletonList(produto));
        when(produtoMapper.toDTO(any(Produto.class))).thenReturn(produtoDTO);

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].nome", is("Cera Modeladora Efeito Seco")));
    }

    @Test
    @DisplayName("Deve buscar um produto por ID com sucesso")
    @WithMockUser
    void deveBuscarProdutoPorIdComSucesso() throws Exception {
        when(produtoService.buscarPorId(1L)).thenReturn(produto);
        when(produtoMapper.toDTO(produto)).thenReturn(produtoDTO);

        mockMvc.perform(get("/produtos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.precoCusto", is(55.90)));
    }

    @Test
    @DisplayName("Deve criar um novo produto com sucesso")
    @WithMockUser
    void deveCriarProdutoComSucesso() throws Exception {
        when(produtoMapper.toEntity(any(ProdutoDTO.class))).thenReturn(produto);
        when(produtoService.criar(any(Produto.class))).thenReturn(produto);
        when(produtoMapper.toDTO(any(Produto.class))).thenReturn(produtoDTO);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/produtos/1"))
                .andExpect(jsonPath("$.message", is("Produto criado com sucesso")));
    }

    @Test
    @DisplayName("Deve atualizar um produto com sucesso")
    @WithMockUser
    void deveAtualizarProdutoComSucesso() throws Exception {
        produtoDTO.setPrecoCusto(new BigDecimal("59.99"));
        produto.setPrecoCusto(new BigDecimal("59.99"));

        when(produtoMapper.toEntity(any(ProdutoDTO.class))).thenReturn(produto);
        when(produtoService.atualizar(eq(1L), any(Produto.class))).thenReturn(produto);
        when(produtoMapper.toDTO(any(Produto.class))).thenReturn(produtoDTO);

        mockMvc.perform(put("/produtos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.precoCusto", is(59.99)))
                .andExpect(jsonPath("$.message", is("Produto atualizado com sucesso")));
    }

    @Test
    @DisplayName("Deve remover um produto com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveRemoverProdutoComSucesso() throws Exception {
        doNothing().when(produtoService).remover(1L);

        mockMvc.perform(delete("/produtos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Produto removido com sucesso")));
    }

    @Test
    @DisplayName("Deve ativar um produto com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveAtivarProdutoComSucesso() throws Exception {
        doNothing().when(produtoService).ativarProduto(1L);

        mockMvc.perform(patch("/produtos/{id}/ativar", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Produto ativado com sucesso")));
    }

    @Test
    @DisplayName("Deve desativar um produto com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveDesativarProdutoComSucesso() throws Exception {
        doNothing().when(produtoService).desativarProduto(1L);

        mockMvc.perform(patch("/produtos/{id}/desativar", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Produto desativado com sucesso")));
    }
}
