package br.org.unicortes.barbearia.services.interfaces;

import br.org.unicortes.barbearia.dtos.AgendamentoDTO;
import br.org.unicortes.barbearia.enums.AgendamentoStatus;
import br.org.unicortes.barbearia.models.Agendamento;

public interface IAgendamentoService {
    Agendamento realizarAgendamento(Agendamento agendamento);
    Agendamento atualizarStatus(Long agendamentoId, AgendamentoStatus novoStatus);
    Agendamento cancelar(Long agendamentoId);
    Agendamento confirmar(Long agendamentoId);
    Agendamento iniciar(Long agendamentoId);
    Agendamento concluir(Long agendamentoId);
}
