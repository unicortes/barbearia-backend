package br.org.unicortes.barbearia.controllers.api;

import br.org.unicortes.barbearia.dtos.CartaoFidelidadeDTO;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/cartoes-fidelidade")
public interface CartaoFidelidadeApi {

    @GetMapping
    @Operation(summary = "Listar todos os cartões de fidelidade", description = "Requer perfil BARBER ou ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de cartões retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<List<CartaoFidelidadeDTO>>> listarTodos();

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cartão de fidelidade por ID", description = "Requer perfil BARBER ou ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cartão encontrado"),
            @ApiResponse(responseCode = "404", description = "Cartão não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<CartaoFidelidadeDTO>> buscarPorId(@PathVariable("id") Long id);

    @PostMapping
    @Operation(summary = "Criar novo cartão de fidelidade", description = "Requer perfil BARBER ou ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cartão criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<AbstractResponse<CartaoFidelidadeDTO>> criar(@RequestBody CartaoFidelidadeDTO dto);

    @PutMapping("/{id}/pontos")
    @Operation(summary = "Adicionar pontos ao cartão de fidelidade", description = "Requer perfil BARBER ou ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pontos adicionados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cartão não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<CartaoFidelidadeDTO>> atualizarPontos(
            @PathVariable("id") Long id,
            @RequestParam("pontos") Integer pontos);

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover cartão de fidelidade", description = "Requer perfil BARBER ou ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cartão removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cartão não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> remover(@PathVariable("id") Long id);
}
