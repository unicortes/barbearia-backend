package br.org.unicortes.barbearia.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
@CrossOrigin(origins = "*")
public class HomeController {

    @GetMapping("/cliente")
    public ResponseEntity<String> clienteHome() {
        return ResponseEntity.ok("Página inicial do cliente");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminHome() {
        return ResponseEntity.ok("Página inicial do administrador");
    }

    @GetMapping("/barbeiro")
    public ResponseEntity<String> barbeiroHome() {
        return ResponseEntity.ok("Página inicial do barbeiro");
    }
}
