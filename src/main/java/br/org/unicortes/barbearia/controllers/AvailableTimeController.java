package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.AvailableTime;
import br.org.unicortes.barbearia.services.AvailableTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/available-times")
public class AvailableTimeController {

    private final AvailableTimeService availableTimeService;

    @Autowired
    public AvailableTimeController(AvailableTimeService availableTimeService) {
        this.availableTimeService = availableTimeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public ResponseEntity<AvailableTime> createAvailableTime(@RequestBody AvailableTime availableTime) {
        AvailableTime createdAvailableTime = availableTimeService.create(availableTime);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAvailableTime);
    }

    @GetMapping("/service/{serviceId}")
    @PreAuthorize("hasRole('BARBER, CLIENT, ADMIN')")
    public ResponseEntity<List<AvailableTime>> getAvailableTimesByServiceId(@PathVariable Long serviceId) {
        List<AvailableTime> availableTimes = availableTimeService.findByServiceId(serviceId);
        return ResponseEntity.ok(availableTimes);
    }

    @GetMapping
    @PreAuthorize("hasRole('BARBER, CLIENT, ADMIN')")
    public ResponseEntity<List<AvailableTime>> getAllAvailableTimes() {
        List<AvailableTime> availableTimes = availableTimeService.getAll();
        return ResponseEntity.ok(availableTimes);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public ResponseEntity<Void> deleteAvailableTime(@PathVariable Long id) {
        availableTimeService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unscheduled")
    @PreAuthorize("hasAnyRole('BARBER', 'CLIENT')")
    public ResponseEntity<List<AvailableTime>> getUnscheduledTimes() {
        List<AvailableTime> unscheduledTimes = availableTimeService.findByIsScheduledFalse();
        return ResponseEntity.ok(unscheduledTimes);
    }
}