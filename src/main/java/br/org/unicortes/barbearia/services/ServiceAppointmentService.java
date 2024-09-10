package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.dtos.ServiceAppointmentDTO;
import br.org.unicortes.barbearia.enums.ServiceAppointmentStatus;
import br.org.unicortes.barbearia.models.*;
import br.org.unicortes.barbearia.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceAppointmentService {

    private final ServiceAppointmentRepository serviceAppointmentRepository;
    private final ServicoRepository servicoRepository;
    private final BarberRepository barberRepository;
    private final ClientRepository clientRepository;
    private final UsuarioRepository usuarioRepository;
    private final AvailableTimeRepository availableTimeRepository;

    @Autowired
    public ServiceAppointmentService(ServiceAppointmentRepository serviceAppointmentRepository,
                                     ServicoRepository servicoRepository,
                                     BarberRepository barberRepository, UsuarioRepository usuarioRepository, ClientRepository clientRepository, AvailableTimeRepository availableTimeRepository) {
        this.serviceAppointmentRepository = serviceAppointmentRepository;
        this.servicoRepository = servicoRepository;
        this.barberRepository = barberRepository;
        this.usuarioRepository = usuarioRepository;
        this.clientRepository=clientRepository;
        this.availableTimeRepository=availableTimeRepository;
    }

    public List<ServiceAppointment> findAll() {
        return serviceAppointmentRepository.findAll();
    }

    public Optional<ServiceAppointment> findById(Long id) {
        return serviceAppointmentRepository.findById(id);
    }

    public List<ServiceAppointment> findByBarberId(Long id) {
        Optional<Usuario> user = this.usuarioRepository.findById(id);
        Optional<Barber> barber = Optional.of(new Barber());
        barber=this.barberRepository.findById(id);

        List<ServiceAppointment>appointments=serviceAppointmentRepository.findByBarberId(barber);
        return appointments;
    }
    public List<ServiceAppointment> findByClientId(Long id) {
        Optional<Usuario> user = this.usuarioRepository.findById(id);
        Optional<Client> client = Optional.of(new Client());
        List<ServiceAppointment>appointments=new ArrayList<>();
        if(user.isPresent()){
            client=this.clientRepository.findById(id);
            appointments=serviceAppointmentRepository.findByClientName(String.valueOf(client));
        }

        return appointments;
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

    public List<ServiceAppointment> findAppointmentsByBarberAndDate(Long barberId, LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);
        return serviceAppointmentRepository.findByBarberIdAndAppointmentDateTimeBetween(barberId, startOfDay, endOfDay);
    }

    public List<ServiceAppointment> getAppointmentsByBarberAndDateTimeRange(Long barberId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return serviceAppointmentRepository.findByBarberIdAndAppointmentDateTimeBetween(barberId, startDateTime, endDateTime);
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

        Optional<Servico> serviceOpt = servicoRepository.findById(dto.getService());
        if (serviceOpt.isPresent()) {
            serviceAppointment.setService(serviceOpt.get());
        } else {
            throw new IllegalArgumentException("Serviço não encontrado para o ID: " + dto.getService());
        }

        Optional<Barber> barberOpt = barberRepository.findById(dto.getBarber());
        if (barberOpt.isPresent()) {
            serviceAppointment.setBarber(barberOpt.get());
        } else {
            throw new IllegalArgumentException("Barbeiro não encontrado para o ID: " + dto.getBarber());
        }

        Optional<AvailableTime> availableTimeOpt = availableTimeRepository.findById(dto.getAvailableTime());
        if (availableTimeOpt.isPresent()) {
            serviceAppointment.setAvailableTime(availableTimeOpt.get());
        } else {
            throw new IllegalArgumentException("Horário disponível não encontrado para o ID: " + dto.getAvailableTime());
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
        dto.setService(entity.getService().getId());
        dto.setBarber(entity.getBarber().getId());
        dto.setClientName(entity.getClientName());
        dto.setAppointmentDateTime(entity.getAppointmentDateTime());
        dto.setStatus(entity.getStatus());
        dto.setAvailableTime(entity.getId());
        dto.setAvailable(entity.isAvailable());

        return dto;
    }
}