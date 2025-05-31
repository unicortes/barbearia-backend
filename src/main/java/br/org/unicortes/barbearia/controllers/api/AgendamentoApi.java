package br.org.unicortes.barbearia.controllers.api;

import br.org.unicortes.barbearia.dtos.AgendamentoDTO;
import br.org.unicortes.barbearia.enums.AgendamentoStatus;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/agendamentos")
public interface AgendamentoApi {

    @PostMapping
    @Operation(summary = "Realizar um novo agendamento")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<AbstractResponse<AgendamentoDTO>> realizarAgendamento(@RequestBody AgendamentoDTO agendamentoDTO);

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do agendamento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Status inválido"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    })
    ResponseEntity<AbstractResponse<AgendamentoDTO>> atualizarStatus(@PathVariable("id") Long id, @RequestParam AgendamentoStatus novoStatus);

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar agendamento")
    ResponseEntity<AbstractResponse<AgendamentoDTO>> cancelar(@PathVariable("id") Long id);

    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar agendamento")
    ResponseEntity<AbstractResponse<AgendamentoDTO>> confirmar(@PathVariable("id") Long id);

    @PatchMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar agendamento")
    ResponseEntity<AbstractResponse<AgendamentoDTO>> iniciar(@PathVariable("id") Long id);

    @PatchMapping("/{id}/concluir")
    @Operation(summary = "Concluir agendamento")
    ResponseEntity<AbstractResponse<AgendamentoDTO>> concluir(@PathVariable("id") Long id);
}
