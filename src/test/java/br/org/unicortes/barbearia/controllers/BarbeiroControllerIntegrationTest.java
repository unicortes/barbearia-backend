package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.BarbeiroDTO;
import br.org.unicortes.barbearia.mappers.IBarbeiroMapper;
import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.services.interfaces.IBarbeiroService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BarbeiroControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IBarbeiroService barbeiroService;

    @MockBean
    private IBarbeiroMapper barbeiroMapper;

    private Barbeiro barbeiro;
    private BarbeiroDTO barbeiroDTO;

    @BeforeEach
    void setUp() {
        barbeiro = new Barbeiro();
        barbeiro.setId(1L);
        barbeiro.setName("João da Silva");
        barbeiro.setAtivo(true);

        barbeiroDTO = new BarbeiroDTO();
        barbeiroDTO.setId(1L);
        barbeiroDTO.setName("João da Silva");
        barbeiroDTO.setAtivo(true);
    }

    @Test
    @DisplayName("Deve listar todos os barbeiros com sucesso")
    @WithMockUser(roles = {"ADMIN"})
    void deveListarTodosOsBarbeirosComSucesso() throws Exception {
        when(barbeiroService.listarTodos()).thenReturn(Collections.singletonList(barbeiro));
        when(barbeiroMapper.toDTO(barbeiro)).thenReturn(barbeiroDTO);

        mockMvc.perform(get("/barbeiros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].name", is("João da Silva")));
    }

    @Test
    @DisplayName("Deve buscar um barbeiro por ID com sucesso")
    @WithMockUser
    void deveBuscarBarbeiroPorIdComSucesso() throws Exception {
        when(barbeiroService.buscarPorId(1L)).thenReturn(barbeiro);
        when(barbeiroMapper.toDTO(barbeiro)).thenReturn(barbeiroDTO);

        mockMvc.perform(get("/barbeiros/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("João da Silva")));
    }

    @Test
    @DisplayName("Deve criar um novo barbeiro com sucesso")
    @WithMockUser(roles = {"ADMIN"})
    void deveCriarBarbeiroComSucesso() throws Exception {
        when(barbeiroMapper.toEntity(any(BarbeiroDTO.class))).thenReturn(barbeiro);
        when(barbeiroService.criar(any(Barbeiro.class))).thenReturn(barbeiro);
        when(barbeiroMapper.toDTO(any(Barbeiro.class))).thenReturn(barbeiroDTO);

        mockMvc.perform(post("/barbeiros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(barbeiroDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/barbeiros/1"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is(barbeiroDTO.getName())))
                .andExpect(jsonPath("$.message", is("Barbeiro criado com sucesso")));
    }

    @Test
    @DisplayName("Deve atualizar um barbeiro com sucesso")
    @WithMockUser(roles = {"ADMIN"})
    void deveAtualizarBarbeiroComSucesso() throws Exception {
        BarbeiroDTO dtoAtualizado = new BarbeiroDTO();
        dtoAtualizado.setId(1L);
        dtoAtualizado.setName("João da Silva Atualizado");
        dtoAtualizado.setAtivo(true);

        Barbeiro barbeiroAtualizado = new Barbeiro();
        barbeiroAtualizado.setId(1L);
        barbeiroAtualizado.setName("João da Silva Atualizado");

        when(barbeiroMapper.toEntity(any(BarbeiroDTO.class))).thenReturn(barbeiroAtualizado);
        when(barbeiroService.atualizar(eq(1L), any(Barbeiro.class))).thenReturn(barbeiroAtualizado);
        when(barbeiroMapper.toDTO(any(Barbeiro.class))).thenReturn(dtoAtualizado);

        mockMvc.perform(put("/barbeiros/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("João da Silva Atualizado")))
                .andExpect(jsonPath("$.message", is("Barbeiro atualizado com sucesso")));
    }

    @Test
    @DisplayName("Deve remover um barbeiro com sucesso")
    @WithMockUser(roles = {"ADMIN"})
    void deveRemoverBarbeiroComSucesso() throws Exception {
        doNothing().when(barbeiroService).remover(1L);

        mockMvc.perform(delete("/barbeiros/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Barbeiro removido com sucesso")));
    }

    @Test
    @DisplayName("Deve ativar um barbeiro com sucesso")
    @WithMockUser(roles = {"ADMIN"})
    void deveAtivarBarbeiroComSucesso() throws Exception {
        doNothing().when(barbeiroService).ativarBarbeiro(1L);

        mockMvc.perform(patch("/barbeiros/{id}/ativar", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Barbeiro ativado com sucesso")));
    }

    @Test
    @DisplayName("Deve desativar um barbeiro com sucesso")
    @WithMockUser(roles = {"ADMIN"})
    void deveDesativarBarbeiroComSucesso() throws Exception {
        doNothing().when(barbeiroService).desativarBarbeiro(1L);

        mockMvc.perform(patch("/barbeiros/{id}/desativar", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Barbeiro desativado com sucesso")));
    }
}
