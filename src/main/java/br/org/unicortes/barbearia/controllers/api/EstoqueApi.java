package br.org.unicortes.barbearia.controllers.api;

import br.org.unicortes.barbearia.dtos.EstoqueDTO;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/estoque")
public interface EstoqueApi {

    @GetMapping
    @Operation(summary = "Listar todos os estoques", description = "Requer perfil BARBER ou ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de estoques retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<List<EstoqueDTO>>> listarTodos();

    @GetMapping("/{id}")
    @Operation(summary = "Buscar estoque por ID", description = "Requer perfil BARBER ou ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estoque encontrado"),
            @ApiResponse(responseCode = "404", description = "Estoque não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<EstoqueDTO>> buscarPorId(@PathVariable("id") Long id);

    @PostMapping
    @Operation(summary = "Criar novo estoque", description = "Requer perfil BARBER ou ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Estoque criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<AbstractResponse<EstoqueDTO>> criar(@RequestBody EstoqueDTO estoqueDTO);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar estoque", description = "Requer perfil BARBER ou ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estoque atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estoque não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<EstoqueDTO>> atualizar(
            @PathVariable("id") Long id,
            @RequestBody EstoqueDTO estoqueDTO);

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover estoque", description = "Requer perfil BARBER ou ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estoque removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estoque não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> remover(@PathVariable("id") Long id);
}
