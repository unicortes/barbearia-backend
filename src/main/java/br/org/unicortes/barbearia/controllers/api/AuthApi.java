package br.org.unicortes.barbearia.controllers.api;

import br.org.unicortes.barbearia.dtos.LoginDTO;
import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
public interface AuthApi {

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Endpoint para teste de acesso admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Acesso permitido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<AbstractResponse<String>> testAdminAccess();

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Registrar novo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    ResponseEntity<AbstractResponse<Usuario>> registerUser(@RequestBody Usuario user);

    @PostMapping("/login")
    @Operation(summary = "Login do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    ResponseEntity<AbstractResponse<LoginDTO>> loginUser(@RequestBody Usuario usuario);

}
