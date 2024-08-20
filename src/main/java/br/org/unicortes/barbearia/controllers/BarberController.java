package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.Barber;
import br.org.unicortes.barbearia.dtos.BarberDTO;
import br.org.unicortes.barbearia.services.BarberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
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
        barberDTO.setId(barber.getId());
        barberDTO.setName(barber.getName());
        barberDTO.setEmail(barber.getEmail());
        barberDTO.setPhone(barber.getPhone());
        barberDTO.setCpf(barber.getCpf());
        barberDTO.setSalary(barber.getSalary());
        barberDTO.setAddress(barber.getAddress());
        barberDTO.setAdmissionDate(barber.getAdmissionDate());
        barberDTO.setOpeningHours(barber.getOpeningHours());
        return barberDTO;
    }

    private Barber convertToEntity(BarberDTO barberDTO) {
        Barber barber = new Barber();
        barber.setId(barberDTO.getId());
        barber.setName(barberDTO.getName());
        barber.setEmail(barberDTO.getEmail());
        barber.setPhone(barberDTO.getPhone());
        barber.setCpf(barberDTO.getCpf());
        barber.setSalary(barberDTO.getSalary());
        barber.setAddress(barberDTO.getAddress());
        barber.setAdmissionDate(barberDTO.getAdmissionDate());
        barber.setOpeningHours(barberDTO.getOpeningHours());
        return barber;
    }
}
