package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.PromocaoDTO;
import br.org.unicortes.barbearia.mappers.IPromocaoMapper;
import br.org.unicortes.barbearia.models.Promocao;
import br.org.unicortes.barbearia.services.interfaces.IPromocaoService;
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

import java.time.LocalDate;
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
class PromocaoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IPromocaoService promocaoService;

    @MockBean
    private IPromocaoMapper promocaoMapper;

    private Promocao promocao;
    private PromocaoDTO promocaoDTO;

    @BeforeEach
    void setUp() {
        promocao = Promocao.builder()
                .id(1L)
                .nome("Promoção de Inverno")
                .descricao("20% de desconto em todos os cortes.")
                .codigoPromo("INVERNO20")
                .categoria("Serviços")
                .desconto(20.0)
                .disponibilidade(true)
                .inicio(LocalDate.now())
                .fim(LocalDate.now().plusMonths(1))
                .build();

        promocaoDTO = PromocaoDTO.builder()
                .id(1L)
                .nome("Promoção de Inverno")
                .descricao("20% de desconto em todos os cortes.")
                .codigoPromo("INVERNO20")
                .categoria("Serviços")
                .desconto(20.0)
                .disponibilidade(true)
                .inicio(promocao.getInicio())
                .fim(promocao.getFim())
                .build();
    }

    @Test
    @DisplayName("Deve listar todas as promoções com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveListarTodasAsPromocoesComSucesso() throws Exception {
        when(promocaoService.listarTodos()).thenReturn(Collections.singletonList(promocao));
        when(promocaoMapper.toDTO(any(Promocao.class))).thenReturn(promocaoDTO);

        mockMvc.perform(get("/promocoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].nome", is("Promoção de Inverno")));
    }

    @Test
    @DisplayName("Deve buscar uma promoção por ID com sucesso")
    @WithMockUser
    void deveBuscarPromocaoPorIdComSucesso() throws Exception {
        when(promocaoService.buscarPorId(1L)).thenReturn(promocao);
        when(promocaoMapper.toDTO(promocao)).thenReturn(promocaoDTO);

        mockMvc.perform(get("/promocoes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.desconto", is(20.0)));
    }

    @Test
    @DisplayName("Deve criar uma nova promoção com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveCriarPromocaoComSucesso() throws Exception {
        when(promocaoMapper.toEntity(any(PromocaoDTO.class))).thenReturn(promocao);
        when(promocaoService.criar(any(Promocao.class))).thenReturn(promocao);
        when(promocaoMapper.toDTO(any(Promocao.class))).thenReturn(promocaoDTO);

        mockMvc.perform(post("/promocoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promocaoDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/promocoes/1"))
                .andExpect(jsonPath("$.message", is("Promoção criada com sucesso")));
    }

    @Test
    @DisplayName("Deve atualizar uma promoção com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarPromocaoComSucesso() throws Exception {
        promocaoDTO.setNome("Super Promoção de Inverno");
        promocao.setNome("Super Promoção de Inverno");

        when(promocaoMapper.toEntity(any(PromocaoDTO.class))).thenReturn(promocao);
        when(promocaoService.atualizar(eq(1L), any(Promocao.class))).thenReturn(promocao);
        when(promocaoMapper.toDTO(any(Promocao.class))).thenReturn(promocaoDTO);

        mockMvc.perform(put("/promocoes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promocaoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome", is("Super Promoção de Inverno")))
                .andExpect(jsonPath("$.message", is("Promoção atualizada com sucesso")));
    }

    @Test
    @DisplayName("Deve remover uma promoção com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveRemoverPromocaoComSucesso() throws Exception {
        doNothing().when(promocaoService).remover(1L);

        mockMvc.perform(delete("/promocoes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Promoção removida com sucesso")));
    }
}
