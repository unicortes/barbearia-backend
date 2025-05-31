package br.org.unicortes.barbearia.controllers.api;

import br.org.unicortes.barbearia.dtos.PromocaoDTO;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/promocoes")
public interface PromocaoApi {

    @GetMapping
    @Operation(summary = "Listar todas as promoções", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de promoções retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<List<PromocaoDTO>>> listarTodos();

    @GetMapping("/{id}")
    @Operation(summary = "Buscar promoção por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Promoção encontrada"),
            @ApiResponse(responseCode = "404", description = "Promoção não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<PromocaoDTO>> buscarPorId(@PathVariable("id") Long id);

    @PostMapping
    @Operation(summary = "Criar nova promoção")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Promoção criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito - promoção já cadastrada")
    })
    ResponseEntity<AbstractResponse<PromocaoDTO>> criar(@RequestBody PromocaoDTO promocaoDTO);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar promoção")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Promoção atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Promoção não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito ao atualizar promoção"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<PromocaoDTO>> atualizar(
            @PathVariable("id") Long id,
            @RequestBody PromocaoDTO promocaoDTO);

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover promoção", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Promoção removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Promoção não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> remover(@PathVariable("id") Long id);
}
