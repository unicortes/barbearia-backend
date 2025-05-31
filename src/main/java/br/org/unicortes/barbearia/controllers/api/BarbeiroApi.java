package br.org.unicortes.barbearia.controllers.api;

import br.org.unicortes.barbearia.dtos.BarbeiroDTO;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/barbeiros")
public interface BarbeiroApi {

    @GetMapping
    @Operation(summary = "Listar todos os barbeiros", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de barbeiros retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<List<BarbeiroDTO>>> listarTodos();

    @GetMapping("/{id}")
    @Operation(summary = "Buscar barbeiro por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Barbeiro encontrado"),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<BarbeiroDTO>> buscarPorId(@PathVariable("id") Long id);

    @PostMapping
    @Operation(summary = "Criar novo barbeiro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Barbeiro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito - barbeiro já existente")
    })
    ResponseEntity<AbstractResponse<BarbeiroDTO>> criar(@RequestBody BarbeiroDTO barbeiroDTO);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar barbeiro")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Barbeiro atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito - dados inconsistentes"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<BarbeiroDTO>> atualizar(
            @PathVariable("id") Long id,
            @RequestBody BarbeiroDTO barbeiroDTO);

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover barbeiro", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Barbeiro removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> remover(@PathVariable("id") Long id);

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar barbeiro", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Barbeiro ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> ativar(@PathVariable("id") Long id);

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar barbeiro", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Barbeiro desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> desativar(@PathVariable("id") Long id);
}
