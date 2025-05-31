package br.org.unicortes.barbearia.controllers.api;

import br.org.unicortes.barbearia.dtos.UsuarioDTO;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import br.org.unicortes.barbearia.requests.NovaSenhaRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/usuarios")
public interface UsuarioApi {

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<List<UsuarioDTO>>> listarTodos();

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<UsuarioDTO>> buscarPorId(@PathVariable("id") Long id);

    @PostMapping
    @Operation(summary = "Criar novo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito - email já em uso")
    })
    ResponseEntity<AbstractResponse<UsuarioDTO>> criar(@RequestBody UsuarioDTO usuarioDTO);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito - email já em uso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<UsuarioDTO>> atualizar(
            @PathVariable("id") Long id,
            @RequestBody UsuarioDTO usuarioDTO);

    @PatchMapping("/{id}/senha")
    @Operation(summary = "Alterar senha")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Senha inválida"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> alterarSenha(
            @PathVariable("id") Long id,
            @RequestBody NovaSenhaRequest novaSenha);

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover usuário", description = "Requer perfil ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<Void>> remover(@PathVariable("id") Long id);
}
