package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.controllers.api.AuthApi;
import br.org.unicortes.barbearia.dtos.LoginDTO;
import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import br.org.unicortes.barbearia.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<AbstractResponse<String>> testAdminAccess() {
        return ResponseEntity.ok(AbstractResponse.success("Admin access granted!"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Usuario>> registerUser(Usuario user) {
        Usuario usuarioCriado = authService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AbstractResponse.success(usuarioCriado, "Usuário criado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<LoginDTO>> loginUser(Usuario usuario) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getPassword())
        );

        Optional<Usuario> usuarioOpt = authService.getUsuarioByEmail(usuario.getEmail());

        return usuarioOpt.map(usuarioAutenticado -> {
            String token = authService.gerarToken(usuarioAutenticado);
            LoginDTO loginDTO = new LoginDTO(usuarioAutenticado, token);
            return ResponseEntity.ok(AbstractResponse.success(loginDTO, "Login realizado com sucesso"));
        }).orElseGet(() ->
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AbstractResponse.error("Usuário não encontrado após autenticação", "USER_NOT_FOUND"))
        );
    }
}
