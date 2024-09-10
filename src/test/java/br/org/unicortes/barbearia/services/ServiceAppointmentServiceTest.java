package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.dtos.ServiceAppointmentDTO;
import br.org.unicortes.barbearia.enums.ServiceAppointmentStatus;
import br.org.unicortes.barbearia.models.ServiceAppointment;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.models.Barber;
import br.org.unicortes.barbearia.repositories.ServiceAppointmentRepository;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import br.org.unicortes.barbearia.repositories.BarberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServiceAppointmentServiceTest {

    @InjectMocks
    private ServiceAppointmentService serviceAppointmentService;

    @Mock
    private ServiceAppointmentRepository serviceAppointmentRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private BarberRepository barberRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        ServiceAppointment appointment1 = new ServiceAppointment();
        ServiceAppointment appointment2 = new ServiceAppointment();
        when(serviceAppointmentRepository.findAll()).thenReturn(List.of(appointment1, appointment2));

        List<ServiceAppointment> result = serviceAppointmentService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(serviceAppointmentRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        ServiceAppointment appointment = new ServiceAppointment();
        when(serviceAppointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        Optional<ServiceAppointment> result = serviceAppointmentService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(appointment, result.get());
        verify(serviceAppointmentRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testUpdateAppointmentStatus() {
        ServiceAppointment appointment = new ServiceAppointment();
        appointment.setStatus(ServiceAppointmentStatus.PENDENTE);
        when(serviceAppointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        when(serviceAppointmentRepository.save(any(ServiceAppointment.class))).thenReturn(appointment);

        ServiceAppointment updatedAppointment = serviceAppointmentService.updateAppointmentStatus(1L, ServiceAppointmentStatus.CONFIRMADO);

        assertEquals(ServiceAppointmentStatus.CONFIRMADO, updatedAppointment.getStatus());
        assertFalse(updatedAppointment.isAvailable());
        verify(serviceAppointmentRepository, times(1)).findById(anyLong());
        verify(serviceAppointmentRepository, times(1)).save(any(ServiceAppointment.class));
    }

    @Test
    public void testConvertToEntity() {
        ServiceAppointmentDTO dto = new ServiceAppointmentDTO();
        dto.setId(1L);
        dto.setService(2L);
        dto.setBarber(3L);
        dto.setClientName("Teste");
        dto.setAppointmentDateTime(LocalDateTime.now());
        dto.setStatus(ServiceAppointmentStatus.PENDENTE);
        dto.setAvailable(true);

        Servico service = new Servico();
        service.setId(2L);
        Barber barber = new Barber();
        barber.setId(3L);

        when(servicoRepository.findById(dto.getService())).thenReturn(Optional.of(service));
        when(barberRepository.findById(dto.getBarber())).thenReturn(Optional.of(barber));

        ServiceAppointment entity = serviceAppointmentService.convertToEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getClientName(), entity.getClientName());
        assertEquals(dto.getAppointmentDateTime(), entity.getAppointmentDateTime());
        assertEquals(dto.getStatus(), entity.getStatus());
        assertEquals(dto.isAvailable(), entity.isAvailable());
        assertEquals(service, entity.getService());
        assertEquals(barber, entity.getBarber());
    }

    @Test
    public void testFindDailyAppointmentsForBarber() {
        Long barberId = 1L;
        LocalDateTime startOfDay = LocalDateTime.of(2024, 8, 30, 0, 0);
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        ServiceAppointment appointment1 = new ServiceAppointment();
        appointment1.setBarber(new Barber());
        appointment1.setAppointmentDateTime(startOfDay.plusHours(2));

        ServiceAppointment appointment2 = new ServiceAppointment();
        appointment2.setBarber(new Barber());
        appointment2.setAppointmentDateTime(startOfDay.plusHours(5));

        when(serviceAppointmentRepository.findByBarberIdAndAppointmentDateTimeBetween(barberId, startOfDay, endOfDay))
                .thenReturn(List.of(appointment1, appointment2));

        List<ServiceAppointment> result = serviceAppointmentService.findAppointmentsByBarberAndDate(barberId, startOfDay);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(serviceAppointmentRepository, times(1))
                .findByBarberIdAndAppointmentDateTimeBetween(barberId, startOfDay, endOfDay);
    }

    @Test
    public void testFindAvailableAppointments() {
        ServiceAppointment appointment1 = new ServiceAppointment();
        appointment1.setAvailable(true);
        ServiceAppointment appointment2 = new ServiceAppointment();
        appointment2.setAvailable(true);
        when(serviceAppointmentRepository.findByServiceIdAndAvailableIsTrue(anyLong())).thenReturn(List.of(appointment1, appointment2));

        List<ServiceAppointment> result = serviceAppointmentService.findAvailableAppointments(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(ServiceAppointment::isAvailable));
        verify(serviceAppointmentRepository, times(1)).findByServiceIdAndAvailableIsTrue(anyLong());
    }

    @Test
    public void testSaveServiceAppointment() {
        ServiceAppointment appointment = new ServiceAppointment();
        when(serviceAppointmentRepository.save(any(ServiceAppointment.class))).thenReturn(appointment);

        ServiceAppointment savedAppointment = serviceAppointmentService.save(appointment);

        assertNotNull(savedAppointment);
        verify(serviceAppointmentRepository, times(1)).save(appointment);
    }

    @Test
    public void testDeleteServiceAppointment() {
        doNothing().when(serviceAppointmentRepository).deleteById(anyLong());

        serviceAppointmentService.deleteById(1L);

        verify(serviceAppointmentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateAppointmentStatusThrowsExceptionWhenNotFound() {
        when(serviceAppointmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                serviceAppointmentService.updateAppointmentStatus(1L, ServiceAppointmentStatus.CONFIRMADO)
        );
    }

    @Test
    public void testFindAllReturnsEmptyList() {
        when(serviceAppointmentRepository.findAll()).thenReturn(List.of());

        List<ServiceAppointment> result = serviceAppointmentService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(serviceAppointmentRepository, times(1)).findAll();
    }

    @Test
    public void testFindByStatus() {
        ServiceAppointment appointment1 = new ServiceAppointment();
        appointment1.setStatus(ServiceAppointmentStatus.PENDENTE);
        ServiceAppointment appointment2 = new ServiceAppointment();
        appointment2.setStatus(ServiceAppointmentStatus.PENDENTE);

        when(serviceAppointmentRepository.findByStatus(ServiceAppointmentStatus.PENDENTE))
                .thenReturn(List.of(appointment1, appointment2));

        List<ServiceAppointment> result = serviceAppointmentService.findByStatus(ServiceAppointmentStatus.PENDENTE);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(a -> a.getStatus() == ServiceAppointmentStatus.PENDENTE));
        verify(serviceAppointmentRepository, times(1)).findByStatus(ServiceAppointmentStatus.PENDENTE);
    }

    @Test
    public void testDeleteServiceAppointmentThrowsExceptionWhenNotFound() {
        doThrow(new RuntimeException("Appointment not found")).when(serviceAppointmentRepository).deleteById(anyLong());

        assertThrows(RuntimeException.class, () -> serviceAppointmentService.deleteById(1L));
    }

    @Test
    public void testFindAvailableAppointmentsReturnsEmptyList() {
        when(serviceAppointmentRepository.findByServiceIdAndAvailableIsTrue(anyLong())).thenReturn(List.of());

        List<ServiceAppointment> result = serviceAppointmentService.findAvailableAppointments(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(serviceAppointmentRepository, times(1)).findByServiceIdAndAvailableIsTrue(anyLong());
    }
}