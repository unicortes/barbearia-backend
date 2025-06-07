package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.HorarioLivreDTO;
import br.org.unicortes.barbearia.mappers.IHorarioLivreMapper;
import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.models.HorarioLivre;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.services.interfaces.IHorarioLivreService;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
class HorarioLivreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IHorarioLivreService horarioLivreService;

    @MockBean
    private IHorarioLivreMapper horarioLivreMapper;

    private HorarioLivre horarioLivre;
    private HorarioLivreDTO horarioLivreDTO;
    private Barbeiro barbeiro;
    private Servico servico;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    @BeforeEach
    void setUp() {
        inicio = LocalDateTime.of(2025, 10, 5, 14, 0);
        fim = LocalDateTime.of(2025, 10, 5, 15, 0);

        barbeiro = new Barbeiro();
        barbeiro.setId(20L);
        barbeiro.setName("José da Navalha");

        servico = new Servico();
        servico.setId(5L);
        servico.setNome("Corte de Cabelo");

        horarioLivre = HorarioLivre.builder()
                .id(1L)
                .barbeiro(barbeiro)
                .servico(servico)
                .inicio(inicio)
                .fim(fim)
                .disponivel(true)
                .build();

        horarioLivreDTO = HorarioLivreDTO.builder()
                .id(1L)
                .barbeiroId(barbeiro.getId())
                .servicoId(servico.getId())
                .inicio(inicio)
                .fim(fim)
                .disponivel(true)
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os horários livres com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveListarTodosOsHorariosLivresComSucesso() throws Exception {
        when(horarioLivreService.listarTodos()).thenReturn(Collections.singletonList(horarioLivre));
        when(horarioLivreMapper.toDTO(any(HorarioLivre.class))).thenReturn(horarioLivreDTO);

        mockMvc.perform(get("/horarios-livres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].servicoId", is(5)));
    }

    @Test
    @DisplayName("Deve buscar um horário livre por ID com sucesso")
    @WithMockUser
    void deveBuscarHorarioLivrePorIdComSucesso() throws Exception {
        when(horarioLivreService.buscarPorId(1L)).thenReturn(horarioLivre);
        when(horarioLivreMapper.toDTO(horarioLivre)).thenReturn(horarioLivreDTO);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        mockMvc.perform(get("/horarios-livres/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.inicio", is(inicio.format(formatter))))
                .andExpect(jsonPath("$.data.fim", is(fim.format(formatter))));
    }

    @Test
    @DisplayName("Deve criar um novo horário livre com sucesso")
    @WithMockUser
    void deveCriarHorarioLivreComSucesso() throws Exception {
        when(horarioLivreMapper.toEntity(any(HorarioLivreDTO.class))).thenReturn(horarioLivre);
        when(horarioLivreService.criar(any(HorarioLivre.class))).thenReturn(horarioLivre);
        when(horarioLivreMapper.toDTO(any(HorarioLivre.class))).thenReturn(horarioLivreDTO);

        mockMvc.perform(post("/horarios-livres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(horarioLivreDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/horarios-livres/1"))
                .andExpect(jsonPath("$.message", is("Horário criado com sucesso")));
    }

    @Test
    @DisplayName("Deve atualizar um horário livre com sucesso")
    @WithMockUser
    void deveAtualizarHorarioLivreComSucesso() throws Exception {
        LocalDateTime novoInicio = inicio.plusHours(1);
        LocalDateTime novoFim = fim.plusHours(1);

        horarioLivreDTO.setInicio(novoInicio);
        horarioLivreDTO.setFim(novoFim);

        HorarioLivre horarioAtualizado = HorarioLivre.builder()
                .id(1L)
                .inicio(novoInicio)
                .fim(novoFim)
                .build();

        HorarioLivreDTO dtoRetornado = new HorarioLivreDTO();
        dtoRetornado.setInicio(novoInicio);
        dtoRetornado.setFim(novoFim);

        when(horarioLivreMapper.toEntity(any(HorarioLivreDTO.class))).thenReturn(horarioAtualizado);
        when(horarioLivreService.atualizar(eq(1L), any(HorarioLivre.class))).thenReturn(horarioAtualizado);
        when(horarioLivreMapper.toDTO(horarioAtualizado)).thenReturn(dtoRetornado);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        mockMvc.perform(put("/horarios-livres/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(horarioLivreDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.inicio", is(novoInicio.format(formatter))))
                .andExpect(jsonPath("$.message", is("Horário atualizado com sucesso")));
    }

    @Test
    @DisplayName("Deve remover um horário livre com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveRemoverHorarioLivreComSucesso() throws Exception {
        doNothing().when(horarioLivreService).remover(1L);

        mockMvc.perform(delete("/horarios-livres/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Horário removido com sucesso")));
    }
}
