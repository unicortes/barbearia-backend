package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.AvailableTimeDTO;
import br.org.unicortes.barbearia.exceptions.ServicoNotFoundException;
import br.org.unicortes.barbearia.services.AvailableTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/available-times")
public class AvailableTimeController {

    @Autowired
    private AvailableTimeService availableTimeService;

    @GetMapping
    public ResponseEntity<List<AvailableTimeDTO>> getAll() {
        List<AvailableTimeDTO> availableTimes = availableTimeService.getAll();
        return ResponseEntity.ok(availableTimes);
    }

    @PostMapping
    public ResponseEntity<AvailableTimeDTO> create(@RequestBody AvailableTimeDTO availableTimeDTO) throws ServicoNotFoundException {
        AvailableTimeDTO createdAvailableTime = availableTimeService.create(availableTimeDTO);
        return ResponseEntity.ok(createdAvailableTime);
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<AvailableTimeDTO>> findByServiceId(@PathVariable Long serviceId) {
        List<AvailableTimeDTO> availableTimes = availableTimeService.findByServiceId(serviceId);
        return ResponseEntity.ok(availableTimes);
    }

    @GetMapping("/scheduled/false")
    public ResponseEntity<List<AvailableTimeDTO>> findByIsScheduledFalse() {
        List<AvailableTimeDTO> availableTimes = availableTimeService.findByIsScheduledFalse();
        return ResponseEntity.ok(availableTimes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        availableTimeService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}