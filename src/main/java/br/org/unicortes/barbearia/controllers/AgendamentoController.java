package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.controllers.api.AgendamentoApi;
import br.org.unicortes.barbearia.dtos.AgendamentoDTO;
import br.org.unicortes.barbearia.enums.AgendamentoStatus;
import br.org.unicortes.barbearia.mappers.IAgendamentoMapper;
import br.org.unicortes.barbearia.models.Agendamento;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import br.org.unicortes.barbearia.services.interfaces.IAgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AgendamentoController implements AgendamentoApi {

    private final IAgendamentoService agendamentoService;
    private final IAgendamentoMapper agendamentoMapper;

    @Override
    public ResponseEntity<AbstractResponse<AgendamentoDTO>> realizarAgendamento(AgendamentoDTO agendamentoDTO) {
        Agendamento agendamento = agendamentoService.realizarAgendamento(agendamentoMapper.toEntity(agendamentoDTO));
        AgendamentoDTO agendamentoDto = agendamentoMapper.toDTO(agendamento);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(agendamento.getId())
                .toUri();

        return ResponseEntity.created(location).body(AbstractResponse.success(agendamentoDto, "Agendamento realizado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<AgendamentoDTO>> atualizarStatus(Long id, AgendamentoStatus novoStatus) {
        Agendamento agendamento = agendamentoService.atualizarStatus(id, novoStatus);
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDTO(agendamento);
        return ResponseEntity.ok(AbstractResponse.success(agendamentoDTO, "Status atualizado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<AgendamentoDTO>> cancelar(Long id) {
        Agendamento agendamento = agendamentoService.cancelar(id);
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDTO(agendamento);
        return ResponseEntity.ok(AbstractResponse.success(agendamentoDTO, "Agendamento cancelado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<AgendamentoDTO>> confirmar(Long id) {
        Agendamento agendamento = agendamentoService.confirmar(id);
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDTO(agendamento);
        return ResponseEntity.ok(AbstractResponse.success(agendamentoDTO, "Agendamento confirmado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<AgendamentoDTO>> iniciar(Long id) {
        Agendamento agendamento = agendamentoService.iniciar(id);
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDTO(agendamento);
        return ResponseEntity.ok(AbstractResponse.success(agendamentoDTO, "Agendamento iniciado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<AgendamentoDTO>> concluir(Long id) {
        Agendamento agendamento = agendamentoService.concluir(id);
        AgendamentoDTO agendamentoDTO = agendamentoMapper.toDTO(agendamento);
        return ResponseEntity.ok(AbstractResponse.success(agendamentoDTO, "Agendamento concluído com sucesso"));
    }
}
