package br.org.unicortes.barbearia.models;

import br.org.unicortes.barbearia.enums.ServiceAppointmentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_service_appointments")
public class ServiceAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Servico service;

    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "appointment_date_time", nullable = false)
    private LocalDateTime appointmentDateTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceAppointmentStatus status;

    @Column(name = "available", nullable = false)
    private boolean available;

    public boolean isEditable() {
        return this.status == ServiceAppointmentStatus.PENDENTE;
    }
}
