package br.org.unicortes.barbearia.controllers.api;

import br.org.unicortes.barbearia.dtos.ProdutoDTO;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/produtos")
public interface ProdutoApi {

    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<List<ProdutoDTO>>> listarTodos();

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<ProdutoDTO>> buscarPorId(@PathVariable("id") Long id);

    @PostMapping
    @Operation(summary = "Criar novo produto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito - produto já cadastrado")
    })
    ResponseEntity<AbstractResponse<ProdutoDTO>> criar(@RequestBody ProdutoDTO produtoDTO);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito ao atualizar produto"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<ProdutoDTO>> atualizar(
            @PathVariable("id") Long id,
            @RequestBody ProdutoDTO produtoDTO);

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover produto", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> remover(@PathVariable("id") Long id);

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar produto", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> ativar(@PathVariable("id") Long id);

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar produto", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> desativar(@PathVariable("id") Long id);
}
