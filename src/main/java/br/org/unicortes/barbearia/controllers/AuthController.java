package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping()
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping
    public String auth() {
        return "Hello World";
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> registerUser(@RequestBody Usuario user) {
        try {
            Usuario usuario = this.authService.createUser(user);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Usuario usuario) {
        this.authService.loadUserByUsername(usuario.getEmail());

        return ResponseEntity.ok("Autenticado com sucesso!");
    }



}
