package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.dtos.ServiceAppointmentDTO;
import br.org.unicortes.barbearia.enums.ServiceAppointmentStatus;
import br.org.unicortes.barbearia.models.ServiceAppointment;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.models.Barber;
import br.org.unicortes.barbearia.repositories.ServiceAppointmentRepository;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import br.org.unicortes.barbearia.repositories.BarberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceAppointmentService {

    private final ServiceAppointmentRepository serviceAppointmentRepository;
    private final ServicoRepository servicoRepository;
    private final BarberRepository barberRepository;

    @Autowired
    public ServiceAppointmentService(ServiceAppointmentRepository serviceAppointmentRepository,
                                     ServicoRepository servicoRepository,
                                     BarberRepository barberRepository) {
        this.serviceAppointmentRepository = serviceAppointmentRepository;
        this.servicoRepository = servicoRepository;
        this.barberRepository = barberRepository;
    }

    public List<ServiceAppointment> findAll() {
        return serviceAppointmentRepository.findAll();
    }

    public Optional<ServiceAppointment> findById(Long id) {
        return serviceAppointmentRepository.findById(id);
    }

    public List<ServiceAppointment> findByBarberId(Long barberId) {
        return serviceAppointmentRepository.findByBarberId(barberId);
    }

    public List<ServiceAppointment> findByStatus(ServiceAppointmentStatus status) {
        return serviceAppointmentRepository.findByStatus(status);
    }

    public List<ServiceAppointment> findByAppointmentDateTimeAfter(LocalDateTime dateTime) {
        return serviceAppointmentRepository.findByAppointmentDateTimeAfter(dateTime);
    }

    public List<ServiceAppointment> findByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end) {
        return serviceAppointmentRepository.findByAppointmentDateTimeBetweenAndAvailableIsTrue(start, end);
    }

    public ServiceAppointment save(ServiceAppointment serviceAppointment) {
        return serviceAppointmentRepository.save(serviceAppointment);
    }

    public void deleteById(Long id) {
        serviceAppointmentRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return serviceAppointmentRepository.existsById(id);
    }

    public ServiceAppointment updateAppointmentStatus(Long appointmentId, ServiceAppointmentStatus newStatus) {
        ServiceAppointment appointment = serviceAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found for the given ID: " + appointmentId));

        ServiceAppointmentStatus currentStatus = appointment.getStatus();

        if (currentStatus == ServiceAppointmentStatus.PENDENTE) {
            if (newStatus == ServiceAppointmentStatus.CONFIRMADO || newStatus == ServiceAppointmentStatus.CANCELADO) {
                appointment.setStatus(newStatus);
                appointment.setAvailable(false);
            } else {
                throw new IllegalArgumentException("Invalid status transition from PENDENTE.");
            }
        } else if (currentStatus == ServiceAppointmentStatus.CONFIRMADO) {
            if (newStatus == ServiceAppointmentStatus.CANCELADO) {
                appointment.setStatus(newStatus);
                appointment.setAvailable(true);
            } else {
                throw new IllegalArgumentException("Invalid status transition from CONFIRMADO.");
            }
        } else {
            throw new IllegalStateException("Invalid status transition for the current state.");
        }

        return serviceAppointmentRepository.save(appointment);
    }


    public List<ServiceAppointment> findAvailableAppointments(Long serviceId) {
        return serviceAppointmentRepository.findByServiceIdAndAvailableIsTrue(serviceId);
    }

    public ServiceAppointment convertToEntity(ServiceAppointmentDTO dto) {
        if (dto == null) {
            return null;
        }

        ServiceAppointment serviceAppointment = new ServiceAppointment();
        serviceAppointment.setId(dto.getId());

        Optional<Servico> serviceOpt = servicoRepository.findById(dto.getServiceId());
        if (serviceOpt.isPresent()) {
            serviceAppointment.setService(serviceOpt.get());
        } else {
            throw new IllegalArgumentException("Serviço não encontrado para o ID: " + dto.getServiceId());
        }

        Optional<Barber> barberOpt = barberRepository.findById(dto.getBarberId());
        if (barberOpt.isPresent()) {
            serviceAppointment.setBarber(barberOpt.get());
        } else {
            throw new IllegalArgumentException("Barbeiro não encontrado para o ID: " + dto.getBarberId());
        }

        serviceAppointment.setClientName(dto.getClientName());
        serviceAppointment.setAppointmentDateTime(dto.getAppointmentDateTime());
        serviceAppointment.setStatus(dto.getStatus());
        serviceAppointment.setAvailable(dto.isAvailable());

        return serviceAppointment;
    }

    public ServiceAppointmentDTO convertToDTO(ServiceAppointment entity) {
        if (entity == null) {
            return null;
        }

        ServiceAppointmentDTO dto = new ServiceAppointmentDTO();
        dto.setId(entity.getId());
        dto.setServiceId(entity.getService().getId());
        dto.setBarberId(entity.getBarber().getId());
        dto.setClientName(entity.getClientName());
        dto.setAppointmentDateTime(entity.getAppointmentDateTime());
        dto.setStatus(entity.getStatus());
        dto.setAvailable(entity.isAvailable());

        return dto;
    }
}
