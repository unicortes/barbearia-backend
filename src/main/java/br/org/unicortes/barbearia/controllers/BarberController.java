package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.Barber;
import br.org.unicortes.barbearia.dtos.BarberDTO;
import br.org.unicortes.barbearia.services.BarberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barber")
public class BarberController {
    @Autowired
    private BarberService barberService;

    @PostMapping
    public ResponseEntity<BarberDTO> createBarber(@RequestBody BarberDTO barberDTO){
        Barber barber = convertToEntity(barberDTO);
        Barber novoBarber = barberService.createBarber(barber);
        return ResponseEntity.ok(convertToDTO(novoBarber));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarberDTO> updateBarber(@PathVariable Long id, @RequestBody BarberDTO barberDTO) {
        Barber barber = convertToEntity(barberDTO);
        Barber barberAtualizado = barberService.updateBarber(id, barber);
        return ResponseEntity.ok(convertToDTO(barberAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBarber(@PathVariable Long id) {
        barberService.deleteBarber(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Barber>> listBarber() {
        List<Barber> barbers = barberService.listAllBarbers();
        return ResponseEntity.ok(barbers);
    }

    private BarberDTO convertToDTO(Barber barber) {
        BarberDTO barberDTO = new BarberDTO();
        barberDTO.setBarberId(barber.getBarberId());
        barberDTO.setNome(barber.getNome());
        barberDTO.setBarberEmail(barber.getBarberEmail());
        barberDTO.setBarberTelefone(barber.getBarberTelefone());
        barberDTO.setBarberCpf(barber.getBarberCpf());
        barberDTO.setBarberSalario(barber.getBarberSalario());
        barberDTO.setBarberEndereco(barber.getBarberEndereco());
        barberDTO.setBarberDataDeAdimissao(barber.getBarberDataDeAdimissao());
        barberDTO.setBarberHorariosAtendimento(barber.getBarberHorariosAtendimento());
        return barberDTO;
    }

    private Barber convertToEntity(BarberDTO barberDTO) {
        Barber barber = new Barber();
        barber.setBarberId(barberDTO.getBarberId());
        barber.setNome(barberDTO.getNome());
        barber.setBarberEmail(barberDTO.getBarberEmail());
        barber.setBarberTelefone(barberDTO.getBarberTelefone());
        barber.setBarberCpf(barberDTO.getBarberCpf());
        barber.setBarberSalario(barberDTO.getBarberSalario());
        barber.setBarberEndereco(barberDTO.getBarberEndereco());
        barber.setBarberDataDeAdimissao(barberDTO.getBarberDataDeAdimissao());
        barber.setBarberHorariosAtendimento(barberDTO.getBarberHorariosAtendimento());
        return barber;
    }
}
