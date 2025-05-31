package br.org.unicortes.barbearia.controllers.api;

import br.org.unicortes.barbearia.dtos.HorarioLivreDTO;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/horarios-livres")
public interface HorarioLivreApi {

    @GetMapping
    @Operation(summary = "Listar todos os horários livres", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de horários livres retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<List<HorarioLivreDTO>>> listarTodos();

    @GetMapping("/{id}")
    @Operation(summary = "Buscar horário livre por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horário livre encontrado"),
            @ApiResponse(responseCode = "404", description = "Horário livre não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<HorarioLivreDTO>> buscarPorId(@PathVariable("id") Long id);

    @PostMapping
    @Operation(summary = "Criar novo horário livre")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Horário livre criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<AbstractResponse<HorarioLivreDTO>> criar(@RequestBody HorarioLivreDTO horarioLivreDTO);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar horário livre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horário livre atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Horário livre não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<AbstractResponse<HorarioLivreDTO>> atualizar(
            @PathVariable("id") Long id,
            @RequestBody HorarioLivreDTO horarioLivreDTO);

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover horário livre", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Horário livre removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Horário livre não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> remover(@PathVariable("id") Long id);
}
