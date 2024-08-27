package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping
    public String auth() {
        return "Hello World";
    }

    @PostMapping("/register")
    public Usuario registerUser(@RequestBody Usuario user) {
        Usuario usuario = this.authService.createUser(user);
        return usuario;
    }
}
