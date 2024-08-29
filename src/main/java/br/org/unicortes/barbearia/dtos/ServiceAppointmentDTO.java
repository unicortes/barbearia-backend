package br.org.unicortes.barbearia.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import br.org.unicortes.barbearia.enums.ServiceAppointmentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceAppointmentDTO {
    private Long id;
    private Long serviceId;
    private Long barberId;
    private String clientName;
    private LocalDateTime appointmentDateTime;
    private ServiceAppointmentStatus status;
    private boolean available;
}
