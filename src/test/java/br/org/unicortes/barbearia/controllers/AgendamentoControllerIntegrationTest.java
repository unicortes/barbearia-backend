package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.models.Cliente;
import br.org.unicortes.barbearia.models.Servico;
import java.math.BigDecimal;

import br.org.unicortes.barbearia.dtos.AgendamentoDTO;
import br.org.unicortes.barbearia.enums.AgendamentoStatus;
import br.org.unicortes.barbearia.mappers.IAgendamentoMapper;
import br.org.unicortes.barbearia.models.Agendamento;
import br.org.unicortes.barbearia.services.interfaces.IAgendamentoService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AgendamentoControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAgendamentoService agendamentoService;

    @MockBean
    private IAgendamentoMapper agendamentoMapper;

    private Agendamento agendamento;
    private AgendamentoDTO agendamentoDTORequest;
    private AgendamentoDTO agendamentoDTOResponse;

    private Cliente cliente;
    private Barbeiro barbeiro;
    private Servico servico;

    @BeforeEach
    void setUp() {

        cliente = new Cliente();
        cliente.setId(10L);
        cliente.setName("Cliente Teste");

        barbeiro = new Barbeiro();
        barbeiro.setId(20L);
        barbeiro.setName("Barbeiro Profissional");

        servico = new Servico();
        servico.setId(30L);
        servico.setNome("Corte e Barba");
        servico.setPreco(new BigDecimal("75.00"));
        servico.setDuracaoPadraoMinutos(45);

        LocalDateTime dataAgendamento = LocalDateTime.of(2025, 8, 10, 15, 30, 0);

        agendamento = Agendamento.builder()
                .id(1L)
                .cliente(cliente)
                .barbeiro(barbeiro)
                .servico(servico)
                .dataHora(dataAgendamento)
                .status(AgendamentoStatus.AGENDADO)
                .build();

        agendamentoDTORequest = AgendamentoDTO.builder()
                .clienteId(cliente.getId())
                .barbeiroId(barbeiro.getId())
                .servicoId(servico.getId())
                .dataHora(dataAgendamento)
                .build();

        agendamentoDTOResponse = AgendamentoDTO.builder()
                .id(agendamento.getId())
                .clienteId(cliente.getId())
                .barbeiroId(barbeiro.getId())
                .servicoId(servico.getId())
                .dataHora(dataAgendamento)
                .status(AgendamentoStatus.AGENDADO)
                .build();
    }

    @Test
    @DisplayName("Deve realizar um agendamento com sucesso e retornar status 201 Created")
    @WithMockUser(username = "user.test", roles = {"CLIENT"})
    void deveRealizarAgendamentoComSucesso() throws Exception {
        when(agendamentoMapper.toEntity(any(AgendamentoDTO.class))).thenReturn(agendamento);
        when(agendamentoService.realizarAgendamento(any(Agendamento.class))).thenReturn(agendamento);
        when(agendamentoMapper.toDTO(any(Agendamento.class))).thenReturn(agendamentoDTOResponse);

        mockMvc.perform(post("/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendamentoDTORequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/agendamentos/1"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Agendamento realizado com sucesso"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.clienteId").value(10L));
    }

    @Test
    @DisplayName("Deve atualizar o status de um agendamento e retornar status 200 OK")
    @WithMockUser(username = "user.test", roles = {"CLIENT"})
    void deveAtualizarStatusComSucesso() throws Exception {
        AgendamentoStatus novoStatus = AgendamentoStatus.CONFIRMADO;
        agendamento.setStatus(novoStatus);
        agendamentoDTOResponse.setStatus(novoStatus);

        when(agendamentoService.atualizarStatus(1L, novoStatus)).thenReturn(agendamento);
        when(agendamentoMapper.toDTO(any(Agendamento.class))).thenReturn(agendamentoDTOResponse);

        mockMvc.perform(patch("/agendamentos/{id}/status", 1L)
                        .param("novoStatus", novoStatus.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Status atualizado com sucesso"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.status").value(novoStatus.toString()));
    }

    @Test
    @DisplayName("Deve cancelar um agendamento com sucesso e retornar status 200 OK")
    @WithMockUser(username = "user.test", roles = {"CLIENT"})
    void deveCancelarAgendamentoComSucesso() throws Exception {
        agendamento.setStatus(AgendamentoStatus.CANCELADO);
        agendamentoDTOResponse.setStatus(AgendamentoStatus.CANCELADO);

        when(agendamentoService.cancelar(1L)).thenReturn(agendamento);
        when(agendamentoMapper.toDTO(any(Agendamento.class))).thenReturn(agendamentoDTOResponse);

        mockMvc.perform(patch("/agendamentos/{id}/cancelar", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Agendamento cancelado com sucesso"))
                .andExpect(jsonPath("$.data.status").value("CANCELADO"));
    }

    @Test
    @DisplayName("Deve confirmar um agendamento com sucesso e retornar status 200 OK")
    @WithMockUser(username = "user.test", roles = {"CLIENT"})
    void deveConfirmarAgendamentoComSucesso() throws Exception {
        agendamento.setStatus(AgendamentoStatus.CONFIRMADO);
        agendamentoDTOResponse.setStatus(AgendamentoStatus.CONFIRMADO);

        when(agendamentoService.confirmar(1L)).thenReturn(agendamento);
        when(agendamentoMapper.toDTO(any(Agendamento.class))).thenReturn(agendamentoDTOResponse);

        mockMvc.perform(patch("/agendamentos/{id}/confirmar", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Agendamento confirmado com sucesso"))
                .andExpect(jsonPath("$.data.status").value("CONFIRMADO"));
    }

    @Test
    @DisplayName("Deve iniciar um agendamento com sucesso e retornar status 200 OK")
    @WithMockUser(username = "user.test", roles = {"CLIENT"})
    void deveIniciarAgendamentoComSucesso() throws Exception {
        agendamento.setStatus(AgendamentoStatus.EM_ANDAMENTO);
        agendamentoDTOResponse.setStatus(AgendamentoStatus.EM_ANDAMENTO);

        when(agendamentoService.iniciar(1L)).thenReturn(agendamento);
        when(agendamentoMapper.toDTO(any(Agendamento.class))).thenReturn(agendamentoDTOResponse);

        mockMvc.perform(patch("/agendamentos/{id}/iniciar", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Agendamento iniciado com sucesso"))
                .andExpect(jsonPath("$.data.status").value("EM_ANDAMENTO"));
    }

    @Test
    @DisplayName("Deve concluir um agendamento com sucesso e retornar status 200 OK")
    @WithMockUser(username = "user.test", roles = {"CLIENT"})
    void deveConcluirAgendamentoComSucesso() throws Exception {
        agendamento.setStatus(AgendamentoStatus.CONCLUIDO);
        agendamentoDTOResponse.setStatus(AgendamentoStatus.CONCLUIDO);

        when(agendamentoService.concluir(1L)).thenReturn(agendamento);
        when(agendamentoMapper.toDTO(any(Agendamento.class))).thenReturn(agendamentoDTOResponse);

        mockMvc.perform(patch("/agendamentos/{id}/concluir", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Agendamento concluído com sucesso"))
                .andExpect(jsonPath("$.data.status").value("CONCLUIDO"));
    }
}
