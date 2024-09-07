package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.ServiceAppointmentDTO;
import br.org.unicortes.barbearia.enums.ServiceAppointmentStatus;
import br.org.unicortes.barbearia.models.ServiceAppointment;
import br.org.unicortes.barbearia.services.ServiceAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class ServiceAppointmentController {

    private final ServiceAppointmentService serviceAppointmentService;

    @Autowired
    public ServiceAppointmentController(ServiceAppointmentService serviceAppointmentService) {
        this.serviceAppointmentService = serviceAppointmentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('BARBER, CLIENT, ADMIN')")
    public ResponseEntity<List<ServiceAppointment>> getAllAppointments() {
        List<ServiceAppointment> appointment = serviceAppointmentService.findAll();
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT, BARBER, ADMIN')")
    public ResponseEntity<ServiceAppointmentDTO> getAppointmentById(@PathVariable Long id) {
        Optional<ServiceAppointment> appointmentOpt = serviceAppointmentService.findById(id);
        return appointmentOpt
                .map(appointment -> ResponseEntity.ok(serviceAppointmentService.convertToDTO(appointment)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/barber/{barberId}")
    @PreAuthorize("hasRole('CLIENT, BARBER, ADMIN')")
    public ResponseEntity<List<ServiceAppointment>> findByBarberId(@PathVariable Long barberId) {
        List<ServiceAppointment> appointments = serviceAppointmentService.findByBarberId(barberId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public ResponseEntity<List<ServiceAppointment>> findByStatus(@PathVariable ServiceAppointmentStatus status) {
        List<ServiceAppointment> appointments = serviceAppointmentService.findByStatus(status);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/after/{dateTime}")
    @PreAuthorize("hasRole('CLIENT, BARBER, ADMIN')")
    public ResponseEntity<List<ServiceAppointment>> findByAppointmentDateTimeAfter(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        List<ServiceAppointment> appointments = serviceAppointmentService.findByAppointmentDateTimeAfter(dateTime);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/between")
    @PreAuthorize("hasRole('CLIENT, BARBER, ADMIN')")
    public ResponseEntity<List<ServiceAppointment>> findByAppointmentDateTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<ServiceAppointment> appointments = serviceAppointmentService.findByAppointmentDateTimeBetween(start, end);
        return ResponseEntity.ok(appointments);
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ServiceAppointmentDTO> createAppointment(@RequestBody ServiceAppointmentDTO dto) {
        ServiceAppointment savedAppointment = serviceAppointmentService.save(serviceAppointmentService.convertToEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceAppointmentService.convertToDTO(savedAppointment));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        if (serviceAppointmentService.existsById(id)) {
            serviceAppointmentService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public ResponseEntity<ServiceAppointmentDTO> updateAppointmentStatus(@PathVariable Long id, @RequestParam ServiceAppointmentStatus status) {
        try {
            ServiceAppointment updatedAppointment = serviceAppointmentService.updateAppointmentStatus(id, status);
            return ResponseEntity.ok(serviceAppointmentService.convertToDTO(updatedAppointment));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('CLIENT, BARBER')")
    public ResponseEntity<List<ServiceAppointmentDTO>> getAvailableAppointments(@RequestParam Long serviceId) {
        List<ServiceAppointmentDTO> availableAppointmentDTOs = serviceAppointmentService.findAvailableAppointments(serviceId).stream()
                .map(serviceAppointmentService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(availableAppointmentDTOs);
    }

    @GetMapping("/barber/{barberId}/daily-schedule")
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public ResponseEntity<List<ServiceAppointmentDTO>> getDailySchedule(
            @PathVariable Long barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime date) {
        List<ServiceAppointment> appointments = serviceAppointmentService.findAppointmentsByBarberAndDate(barberId, date);
        List<ServiceAppointmentDTO> appointmentDTOs = appointments.stream()
                .map(serviceAppointmentService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentDTOs);
    }

    @GetMapping("/barber/timeRange/{barberId}")
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public List<ServiceAppointment> getAppointmentsByBarberAndDateTimeRange(
            @PathVariable Long barberId,
            @RequestParam LocalDateTime startDateTime,
            @RequestParam LocalDateTime endDateTime) {
        return serviceAppointmentService.getAppointmentsByBarberAndDateTimeRange(barberId, startDateTime, endDateTime);
    }
}