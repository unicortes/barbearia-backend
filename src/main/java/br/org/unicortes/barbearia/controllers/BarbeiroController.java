package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.models.Product;
import br.org.unicortes.barbearia.dtos.BarbeiroDTO;
import br.org.unicortes.barbearia.services.BarbeiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        barbeiroDTO.setBarbeiroId(barbeiro.getBarbeiroById());
        barbeiroDTO.setBarbeiroNome(barbeiro.getBarbeiroByNome());
        barbeiroDTO.setBarbeiroEmail(barbeiro.getBarbeiroByEmail());
        barbeiroDTO.setBarbeiroTelefone(barbeiro.getBarbeiroByTelefone());
        barbeiroDTO.setBarbeiroCpf(barbeiro.getBarbeiroByCpf());
        barbeiroDTO.setBarbeiroSalario(barbeiro.getBarbeiroBySalario());
        barbeiroDTO.setBarbeiroEndereco(barbeiro.getBarbeiroByEndereco());
        barbeiroDTO.setBarbeiroDataDeAdimissao(barbeiro.getBarbeiroByDataAdmissao());
        barbeiroDTO.setBarbeiroHorariosAtendimento(barbeiro.getBarbeiroByHorariosAtendimento());
        return barbeiroDTO;
    }

    private Barbeiro convertToEntity(BarbeiroDTO barbeiroDTO) {
        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setBarbeiroById(barbeiroDTO.getBarbeiroId());
        barbeiro.setBarbeiroByNome(barbeiroDTO.getBarbeiroNome());
        barbeiro.setBarbeiroByEmail(barbeiroDTO.getBarbeiroEmail());
        barbeiro.setBarbeiroByTelefone(barbeiroDTO.getBarbeiroTelefone());
        barbeiro.setBarbeiroByCpf(barbeiroDTO.getBarbeiroCpf());
        barbeiro.setBarbeiroBySalario(barbeiroDTO.getBarbeiroSalario());
        barbeiro.setBarbeiroByEndereco(barbeiroDTO.getBarbeiroEndereco());
        barbeiro.setBarbeiroByDataAdmissao(barbeiroDTO.getBarbeiroDataDeAdimissao());
        barbeiro.setBarbeiroByHorariosAtendimento(barbeiroDTO.getBarbeiroHorariosAtendimento());
        return barbeiro;
    }
}
