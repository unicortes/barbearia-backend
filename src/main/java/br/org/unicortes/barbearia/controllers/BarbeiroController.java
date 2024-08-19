package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.dtos.BarbeiroDTO;
import br.org.unicortes.barbearia.services.BarbeiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbeiros")
public class BarbeiroController {
    @Autowired
    private BarbeiroService barbeiroService;

    @PostMapping
    public ResponseEntity<BarbeiroDTO> createBarbeiro(@RequestBody BarbeiroDTO barbeiroDTO){
        Barbeiro barbeiro = convertToEntity(barbeiroDTO);
        Barbeiro novoBarbeiro = barbeiroService.createBarbeiro(barbeiro);
        return ResponseEntity.ok(convertToDTO(novoBarbeiro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarbeiroDTO> updateBarbeiro(@PathVariable Long id, @RequestBody BarbeiroDTO barbeiroDTO) {
        Barbeiro barbeiro = convertToEntity(barbeiroDTO);
        Barbeiro barbeiroAtualizado = barbeiroService.updateBarbeiro(id, barbeiro);
        return ResponseEntity.ok(convertToDTO(barbeiroAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBarbeiro(@PathVariable Long id) {
        barbeiroService.deleteBarbeiro(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Barbeiro>> listarBarbeiros() {
        List<Barbeiro> barbeiros = barbeiroService.listarTodosBarbeiros();
        return ResponseEntity.ok(barbeiros);
    }

    private BarbeiroDTO convertToDTO(Barbeiro barbeiro) {
        BarbeiroDTO barbeiroDTO = new BarbeiroDTO();
        barbeiroDTO.setBarbeiroId(barbeiro.getBarbeiroId());
        barbeiroDTO.setBarbeiroNome(barbeiro.getBarbeiroNome());
        barbeiroDTO.setBarbeiroEmail(barbeiro.getBarbeiroEmail());
        barbeiroDTO.setBarbeiroTelefone(barbeiro.getBarbeiroTelefone());
        barbeiroDTO.setBarbeiroCpf(barbeiro.getBarbeiroCpf());
        barbeiroDTO.setBarbeiroSalario(barbeiro.getBarbeiroSalario());
        barbeiroDTO.setBarbeiroEndereco(barbeiro.getBarbeiroEndereco());
        barbeiroDTO.setBarbeiroDataDeAdimissao(barbeiro.getBarbeiroDataDeAdimissao());
        barbeiroDTO.setBarbeiroHorariosAtendimento(barbeiro.getBarbeiroHorariosAtendimento());
        return barbeiroDTO;
    }

    private Barbeiro convertToEntity(BarbeiroDTO barbeiroDTO) {
        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setBarbeiroId(barbeiroDTO.getBarbeiroId());
        barbeiro.setBarbeiroNome(barbeiroDTO.getBarbeiroNome());
        barbeiro.setBarbeiroEmail(barbeiroDTO.getBarbeiroEmail());
        barbeiro.setBarbeiroTelefone(barbeiroDTO.getBarbeiroTelefone());
        barbeiro.setBarbeiroCpf(barbeiroDTO.getBarbeiroCpf());
        barbeiro.setBarbeiroSalario(barbeiroDTO.getBarbeiroSalario());
        barbeiro.setBarbeiroEndereco(barbeiroDTO.getBarbeiroEndereco());
        barbeiro.setBarbeiroDataDeAdimissao(barbeiroDTO.getBarbeiroDataDeAdimissao());
        barbeiro.setBarbeiroHorariosAtendimento(barbeiroDTO.getBarbeiroHorariosAtendimento());
        return barbeiro;
    }
}
