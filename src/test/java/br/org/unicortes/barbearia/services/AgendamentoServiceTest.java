package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.enums.AgendamentoStatus;
import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.*;
import br.org.unicortes.barbearia.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @InjectMocks
    private AgendamentoService service;

    @Mock
    private AgendamentoRepository agendamentoRepository;
    @Mock
    private ServicoRepository servicoRepository;
    @Mock
    private BarbeiroRepository barbeiroRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private HorarioLivreRepository horarioLivreRepository;

    private Agendamento agendamento;
    private HorarioLivre horarioLivre;
    private Servico servico;
    private Barbeiro barbeiro;
    private Cliente cliente;

    @BeforeEach
    void setup() {
        servico = new Servico();
        servico.setId(1L);

        barbeiro = new Barbeiro();
        barbeiro.setId(1L);

        cliente = new Cliente();
        cliente.setId(1L);

        horarioLivre = new HorarioLivre();
        horarioLivre.setId(1L);
        horarioLivre.setDisponivel(true);

        agendamento = new Agendamento();
        agendamento.setId(1L);
        agendamento.setServico(servico);
        agendamento.setBarbeiro(barbeiro);
        agendamento.setCliente(cliente);
        agendamento.setHorarioLivre(horarioLivre);
        agendamento.setDataHora(LocalDateTime.now().plusDays(1));
        agendamento.setStatus(AgendamentoStatus.AGENDADO);
    }

    @Test
    void realizarAgendamento_Sucesso() {
        when(servicoRepository.findById(servico.getId())).thenReturn(Optional.of(servico));
        when(barbeiroRepository.findById(barbeiro.getId())).thenReturn(Optional.of(barbeiro));
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(horarioLivreRepository.findById(horarioLivre.getId())).thenReturn(Optional.of(horarioLivre));
        when(agendamentoRepository.save(any(Agendamento.class))).thenAnswer(i -> i.getArgument(0));
        when(horarioLivreRepository.save(any(HorarioLivre.class))).thenAnswer(i -> i.getArgument(0));

        Agendamento resultado = service.realizarAgendamento(agendamento);

        assertNotNull(resultado);
        assertFalse(horarioLivre.isDisponivel());
        verify(horarioLivreRepository).save(horarioLivre);
        verify(agendamentoRepository).save(agendamento);
    }

    @Test
    void realizarAgendamento_HorarioLivreNaoEncontrado() {
        when(servicoRepository.findById(servico.getId())).thenReturn(Optional.of(servico));
        when(barbeiroRepository.findById(barbeiro.getId())).thenReturn(Optional.of(barbeiro));
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(horarioLivreRepository.findById(horarioLivre.getId())).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.realizarAgendamento(agendamento));
        assertEquals("Horário livre não encontrado.", exception.getMessage());
    }

    @Test
    void atualizarStatus_TransicaoValida_Sucesso() {
        agendamento.setStatus(AgendamentoStatus.AGENDADO);
        when(agendamentoRepository.findById(agendamento.getId())).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Agendamento resultado = service.atualizarStatus(agendamento.getId(), AgendamentoStatus.CONFIRMADO);

        assertEquals(AgendamentoStatus.CONFIRMADO, resultado.getStatus());
    }

    @Test
    void atualizarStatus_TransicaoInvalida() {
        agendamento.setStatus(AgendamentoStatus.CONCLUIDO);
        when(agendamentoRepository.findById(agendamento.getId())).thenReturn(Optional.of(agendamento));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.atualizarStatus(agendamento.getId(), AgendamentoStatus.CONFIRMADO));
        assertTrue(exception.getMessage().contains("Transição de status inválida"));
    }

    @Test
    void cancelar_StatusCancelavel_Sucesso() {
        agendamento.setStatus(AgendamentoStatus.AGENDADO);
        when(agendamentoRepository.findById(agendamento.getId())).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(horarioLivreRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Agendamento resultado = service.cancelar(agendamento.getId());

        assertEquals(AgendamentoStatus.CANCELADO, resultado.getStatus());
        assertTrue(resultado.getHorarioLivre().isDisponivel());
    }

    @Test
    void cancelar_StatusNaoCancelavel() {
        agendamento.setStatus(AgendamentoStatus.CONCLUIDO);
        when(agendamentoRepository.findById(agendamento.getId())).thenReturn(Optional.of(agendamento));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.cancelar(agendamento.getId()));
        assertTrue(exception.getMessage().contains("Não é possível cancelar"));
    }

    @Test
    void confirmar_StatusConfirmavel_Sucesso() {
        agendamento.setStatus(AgendamentoStatus.AGENDADO);
        when(agendamentoRepository.findById(agendamento.getId())).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Agendamento resultado = service.confirmar(agendamento.getId());

        assertEquals(AgendamentoStatus.CONFIRMADO, resultado.getStatus());
    }

    @Test
    void confirmar_StatusNaoConfirmavel() {
        agendamento.setStatus(AgendamentoStatus.CONCLUIDO);
        when(agendamentoRepository.findById(agendamento.getId())).thenReturn(Optional.of(agendamento));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.confirmar(agendamento.getId()));
        assertTrue(exception.getMessage().contains("Não é possível confirmar"));
    }

    @Test
    void iniciar_StatusIniciavel_Sucesso() {
        agendamento.setStatus(AgendamentoStatus.CONFIRMADO);
        when(agendamentoRepository.findById(agendamento.getId())).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Agendamento resultado = service.iniciar(agendamento.getId());

        assertEquals(AgendamentoStatus.EM_ANDAMENTO, resultado.getStatus());
    }

    @Test
    void iniciar_StatusNaoIniciavel() {
        agendamento.setStatus(AgendamentoStatus.AGENDADO);
        when(agendamentoRepository.findById(agendamento.getId())).thenReturn(Optional.of(agendamento));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.iniciar(agendamento.getId()));
        assertTrue(exception.getMessage().contains("Não é possível iniciar"));
    }

    @Test
    void concluir_StatusConcluivel_Sucesso() {
        agendamento.setStatus(AgendamentoStatus.EM_ANDAMENTO);
        when(agendamentoRepository.findById(agendamento.getId())).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Agendamento resultado = service.concluir(agendamento.getId());

        assertEquals(AgendamentoStatus.CONCLUIDO, resultado.getStatus());
    }

    @Test
    void concluir_StatusNaoConcluivel() {
        agendamento.setStatus(AgendamentoStatus.AGENDADO);
        when(agendamentoRepository.findById(agendamento.getId())).thenReturn(Optional.of(agendamento));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.concluir(agendamento.getId()));
        assertTrue(exception.getMessage().contains("Não é possível concluir"));
    }

    @Test
    void buscarAgendamento_NaoEncontrado() {
        when(agendamentoRepository.findById(999L)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.atualizarStatus(999L, AgendamentoStatus.CONFIRMADO));
        assertTrue(exception.getMessage().contains("Agendamento não encontrado"));
    }

    @Test
    void realizarAgendamento_DataHoraNula() {
        agendamento.setDataHora(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.realizarAgendamento(agendamento));
        assertEquals("Data do agendamento deve ser atual ou futura.", ex.getMessage());
    }

    @Test
    void realizarAgendamento_DataHoraPassada() {
        agendamento.setDataHora(LocalDateTime.now().minusDays(1));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.realizarAgendamento(agendamento));
        assertEquals("Data do agendamento deve ser atual ou futura.", ex.getMessage());
    }

    @Test
    void realizarAgendamento_EntidadeNaoEncontrada() {
        when(servicoRepository.findById(servico.getId())).thenReturn(Optional.empty());
        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.realizarAgendamento(agendamento));
        assertEquals("Serviço não encontrado.", ex.getMessage());
    }
}
