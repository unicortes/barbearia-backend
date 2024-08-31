package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.Barber;
import br.org.unicortes.barbearia.dtos.BarberDTO;
import br.org.unicortes.barbearia.services.BarberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/barber")
public class BarberController {

    @Autowired
    private BarberService barberService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BarberDTO> createBarber(@RequestBody BarberDTO barberDTO){
        Barber barber = convertToEntity(barberDTO);
        Barber novoBarber = barberService.createBarber(barber);
        return ResponseEntity.ok(convertToDTO(novoBarber));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BarberDTO> updateBarber(@PathVariable Long id, @RequestBody BarberDTO barberDTO) {
        Barber barber = convertToEntity(barberDTO);
        Barber barberAtualizado = barberService.updateBarber(id, barber);
        return ResponseEntity.ok(convertToDTO(barberAtualizado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarBarber(@PathVariable Long id) {
        barberService.deleteBarber(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Barber>> listBarber() {
        List<Barber> barbers = barberService.listAllBarbers();
        return ResponseEntity.ok(barbers);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BarberDTO> getBarberById(@PathVariable Long id) {
        Barber barber = barberService.getBarberById(id);
        return ResponseEntity.ok(convertToDTO(barber));
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
        if (barberDTO.getId() != null) {
            barber.setId(barberDTO.getId());
        }
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