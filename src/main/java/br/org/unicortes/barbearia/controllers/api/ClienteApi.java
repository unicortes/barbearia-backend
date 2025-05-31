package br.org.unicortes.barbearia.controllers.api;

import br.org.unicortes.barbearia.dtos.ClienteDTO;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/clientes")
public interface ClienteApi {

    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<List<ClienteDTO>>> listarTodos();

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<ClienteDTO>> buscarPorId(@PathVariable("id") Long id);

    @PostMapping
    @Operation(summary = "Criar novo cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito - email já em uso")
    })
    ResponseEntity<AbstractResponse<ClienteDTO>> criar(@RequestBody ClienteDTO clientDTO);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito - email já em uso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<ClienteDTO>> atualizar(
            @PathVariable("id") Long id,
            @RequestBody ClienteDTO clientDTO);

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover cliente", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> remover(@PathVariable("id") Long id);

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar cliente", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> ativar(@PathVariable("id") Long id);

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar cliente", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> desativar(@PathVariable("id") Long id);
}
