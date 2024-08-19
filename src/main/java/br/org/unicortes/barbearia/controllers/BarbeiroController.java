package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.services.BarbeiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/barbeiros")
public class BarbeiroController {
    @Autowired
    private BarbeiroService barbeiroService;

    @PostMapping
    public ResponseEntity<Barbeiro> criarBarbeiro(@RequestBody Barbeiro barbeiro) {
        Barbeiro novoBarbeiro = barbeiroService.registrarBarbeiro(barbeiro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoBarbeiro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Barbeiro> atualizarBarbeiro(@PathVariable Long id, @RequestBody Barbeiro barbeiro) {
        Barbeiro barbeiroAtualizado = barbeiroService.editarBarbeiro(id, barbeiro);
        return ResponseEntity.ok(barbeiroAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBarbeiro(@PathVariable Long id) {
        barbeiroService.removerBarbeiro(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Barbeiro>> listarBarbeiros() {
        List<Barbeiro> barbeiros = barbeiroService.listarTodosBarbeiros();
        return ResponseEntity.ok(barbeiros);
    }
}
