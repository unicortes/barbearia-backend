package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.ServicoDTO;
import br.org.unicortes.barbearia.mappers.IServicoMapper;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.services.interfaces.IServicoService;
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
class ServicoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IServicoService servicoService;

    @MockBean
    private IServicoMapper servicoMapper;

    private Servico servico;
    private ServicoDTO servicoDTO;

    @BeforeEach
    void setUp() {
        servico = Servico.builder()
                .id(1L)
                .nome("Corte Masculino Moderno")
                .descricao("Corte com tesoura e máquina, finalizado com lavagem e modelagem.")
                .preco(new BigDecimal("60.00"))
                .duracaoPadraoMinutos(45)
                .ativo(true)
                .build();

        servicoDTO = ServicoDTO.builder()
                .id(1L)
                .nome("Corte Masculino Moderno")
                .descricao("Corte com tesoura e máquina, finalizado com lavagem e modelagem.")
                .preco(new BigDecimal("60.00"))
                .duracaoPadraoMinutos(45)
                .ativo(true)
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os serviços com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveListarTodosOsServicosComSucesso() throws Exception {
        when(servicoService.listarTodos()).thenReturn(Collections.singletonList(servico));
        when(servicoMapper.toDTO(any(Servico.class))).thenReturn(servicoDTO);

        mockMvc.perform(get("/servicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].nome", is("Corte Masculino Moderno")));
    }

    @Test
    @DisplayName("Deve buscar um serviço por ID com sucesso")
    @WithMockUser
    void deveBuscarServicoPorIdComSucesso() throws Exception {
        when(servicoService.buscarPorId(1L)).thenReturn(servico);
        when(servicoMapper.toDTO(servico)).thenReturn(servicoDTO);

        mockMvc.perform(get("/servicos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.preco", is(60.00)));
    }

    @Test
    @DisplayName("Deve criar um novo serviço com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveCriarServicoComSucesso() throws Exception {
        when(servicoMapper.toEntity(any(ServicoDTO.class))).thenReturn(servico);
        when(servicoService.criar(any(Servico.class))).thenReturn(servico);
        when(servicoMapper.toDTO(any(Servico.class))).thenReturn(servicoDTO);

        mockMvc.perform(post("/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicoDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/servicos/1"))
                .andExpect(jsonPath("$.message", is("Serviço criado com sucesso")));
    }

    @Test
    @DisplayName("Deve atualizar um serviço com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarServicoComSucesso() throws Exception {
        servicoDTO.setPreco(new BigDecimal("65.00"));
        servico.setPreco(new BigDecimal("65.00"));

        when(servicoMapper.toEntity(any(ServicoDTO.class))).thenReturn(servico);
        when(servicoService.atualizar(eq(1L), any(Servico.class))).thenReturn(servico);
        when(servicoMapper.toDTO(any(Servico.class))).thenReturn(servicoDTO);

        mockMvc.perform(put("/servicos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.preco", is(65.00)))
                .andExpect(jsonPath("$.message", is("Serviço atualizado com sucesso")));
    }

    @Test
    @DisplayName("Deve remover um serviço com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveRemoverServicoComSucesso() throws Exception {
        doNothing().when(servicoService).remover(1L);

        mockMvc.perform(delete("/servicos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Serviço removido com sucesso")));
    }
}
