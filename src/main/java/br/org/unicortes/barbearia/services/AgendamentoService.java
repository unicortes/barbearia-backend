package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.enums.AgendamentoStatus;
import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.Agendamento;
import br.org.unicortes.barbearia.models.HorarioLivre;
import br.org.unicortes.barbearia.repositories.*;
import br.org.unicortes.barbearia.services.interfaces.IAgendamentoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgendamentoService implements IAgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ServicoRepository servicoRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final ClienteRepository clienteRepository;
    private final HorarioLivreRepository horarioLivreRepository;

    @Override
    @Transactional
    public Agendamento realizarAgendamento(Agendamento agendamento) {
        validarDadosAgendamento(agendamento);

        marcarHorarioComoIndisponivel(agendamento.getHorarioLivre().getId());

        return agendamentoRepository.save(agendamento);
    }

    @Override
    @Transactional
    public Agendamento atualizarStatus(Long agendamentoId, AgendamentoStatus novoStatus) {
        Agendamento agendamento = buscarAgendamento(agendamentoId);
        AgendamentoStatus statusAtual = agendamento.getStatus();

        if (!AgendamentoStatus.isStatusValido(statusAtual, novoStatus)) {
            throw new IllegalStateException("Transição de status inválida: de " + statusAtual + " para " + novoStatus);
        }

        agendamento.setStatus(novoStatus);
        return agendamentoRepository.save(agendamento);
    }

    @Override
    @Transactional
    public Agendamento cancelar(Long agendamentoId) {
        Agendamento agendamento = buscarAgendamento(agendamentoId);
        if (!podeCancelar(agendamento.getStatus())) {
            throw new IllegalStateException("Não é possível cancelar este agendamento.");
        }

        agendamento.setStatus(AgendamentoStatus.CANCELADO);
        liberarHorario(agendamento.getHorarioLivre());
        return agendamentoRepository.save(agendamento);
    }

    @Override
    @Transactional
    public Agendamento confirmar(Long agendamentoId) {
        Agendamento agendamento = buscarAgendamento(agendamentoId);
        if (!podeConfirmar(agendamento.getStatus())) {
            throw new IllegalStateException("Não é possível confirmar este agendamento.");
        }

        agendamento.setStatus(AgendamentoStatus.CONFIRMADO);
        return agendamentoRepository.save(agendamento);
    }

    @Override
    @Transactional
    public Agendamento iniciar(Long agendamentoId) {
        Agendamento agendamento = buscarAgendamento(agendamentoId);
        if (!podeIniciar(agendamento.getStatus())) {
            throw new IllegalStateException("Não é possível iniciar este agendamento.");
        }

        agendamento.setStatus(AgendamentoStatus.EM_ANDAMENTO);
        return agendamentoRepository.save(agendamento);
    }

    @Override
    @Transactional
    public Agendamento concluir(Long agendamentoId) {
        Agendamento agendamento = buscarAgendamento(agendamentoId);
        if (!podeConcluir(agendamento.getStatus())) {
            throw new IllegalStateException("Não é possível concluir este agendamento.");
        }

        agendamento.setStatus(AgendamentoStatus.CONCLUIDO);
        return agendamentoRepository.save(agendamento);
    }

    private void validarDadosAgendamento(Agendamento agendamento) {
        if (agendamento == null) {
            throw new IllegalArgumentException("Dados do agendamento não podem ser nulos.");
        }

        if (agendamento.getDataHora() == null || agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data do agendamento deve ser atual ou futura.");
        }

        verificarEntidade(servicoRepository.findById(agendamento.getServico().getId()), "Serviço não encontrado.");
        verificarEntidade(barbeiroRepository.findById(agendamento.getBarbeiro().getId()), "Barbeiro não encontrado.");
        verificarEntidade(clienteRepository.findById(agendamento.getCliente().getId()), "Cliente não encontrado.");
        verificarEntidade(horarioLivreRepository.findById(agendamento.getHorarioLivre().getId()), "Horário livre não encontrado.");
    }

    private void verificarEntidade(Optional<?> entidade, String mensagemErro) {
        if (entidade.isEmpty()) {
            throw new EntidadeNaoEncontradaException(mensagemErro);
        }
    }

    private Agendamento buscarAgendamento(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Agendamento não encontrado com ID: " + id));
    }

    private void marcarHorarioComoIndisponivel(Long horarioLivreId) {
        HorarioLivre horario = horarioLivreRepository.findById(horarioLivreId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Horário livre não encontrado."));
        horario.setDisponivel(false);
        horarioLivreRepository.save(horario);
    }

    private void liberarHorario(HorarioLivre horario) {
        horario.setDisponivel(true);
        horarioLivreRepository.save(horario);
    }

    private boolean podeCancelar(AgendamentoStatus status) {
        return status.podeCancelar();
    }

    private boolean podeConfirmar(AgendamentoStatus status) {
        return status.podeConfirmar();
    }

    private boolean podeIniciar(AgendamentoStatus status) {
        return status.podeIniciar();
    }

    private boolean podeConcluir(AgendamentoStatus status) {
        return status.podeConcluir();
    }
}
