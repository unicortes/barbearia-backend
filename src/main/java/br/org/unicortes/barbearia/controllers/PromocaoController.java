package br.org.unicortes.barbearia.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.org.unicortes.barbearia.models.Promocao;
import br.org.unicortes.barbearia.services.PromocaoService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/promocoes/")
public class PromocaoController {

    @Autowired
    private PromocaoService promocaoService;

    @GetMapping("/index")
	public List<Promocao> getAllPromocao() {
        return promocaoService.getAllPromocao();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promocao> getPromocaoById(@PathVariable Long id) {
        Optional<Promocao> promocao = promocaoService.getPromocaoById(id);
        return promocao
                .map(promocaoItem -> ResponseEntity.ok(promocaoItem))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping("/criar")
    public ResponseEntity<Promocao> criarPromocao(@RequestBody Promocao promocao) {
        Promocao novaPromocao = promocaoService.savePromocao(promocao);
        return new ResponseEntity<>(novaPromocao, HttpStatus.CREATED);
    }

    @PatchMapping("/editar/{id}")
    public ResponseEntity<Promocao> editarPromocao(@PathVariable Long id, @RequestBody Promocao promocao) {
        try {
            Promocao promocaoAtualizada = promocaoService.updatePromocao(id, promocao);
            return new ResponseEntity<>(promocaoAtualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPromocao(@PathVariable Long id) {
        try {
            promocaoService.deletePromocao(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
