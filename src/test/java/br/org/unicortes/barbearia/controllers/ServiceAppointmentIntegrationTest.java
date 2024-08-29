package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.ServiceAppointmentDTO;
import br.org.unicortes.barbearia.enums.ServiceAppointmentStatus;
import br.org.unicortes.barbearia.models.Barber;
import br.org.unicortes.barbearia.models.ServiceAppointment;
import br.org.unicortes.barbearia.services.ServiceAppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServiceAppointmentController.class)
public class ServiceAppointmentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceAppointmentService serviceAppointmentService;

    private ServiceAppointment serviceAppointment;

    private ServiceAppointmentDTO serviceAppointmentDTO;

    @BeforeEach
    public void setup() {
        serviceAppointment = ServiceAppointment.builder()
                .id(1L)
                .service(null)
                .barber(null)
                .clientName("John Doe")
                .appointmentDateTime(LocalDateTime.now())
                .status(ServiceAppointmentStatus.PENDENTE)
                .available(true)
                .build();

        serviceAppointmentDTO = ServiceAppointmentDTO.builder()
                .id(1L)
                .serviceId(1L)
                .barberId(1L)
                .clientName("John Doe")
                .appointmentDateTime(LocalDateTime.now())
                .status(ServiceAppointmentStatus.PENDENTE)
                .available(true)
                .build();
    }

    @Test
    public void testGetAllAppointments() throws Exception {
        List<ServiceAppointment> appointments = Arrays.asList(serviceAppointment);
        List<ServiceAppointmentDTO> appointmentDTOs = Arrays.asList(serviceAppointmentDTO);

        when(serviceAppointmentService.findAll()).thenReturn(appointments);

        when(serviceAppointmentService.convertToDTO(serviceAppointment)).thenReturn(serviceAppointmentDTO);

        mockMvc.perform(get("/api/appointments")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(serviceAppointmentDTO.getId()))
                .andExpect(jsonPath("$[0].clientName").value(serviceAppointmentDTO.getClientName()));
    }


    @Test
    public void testGetAppointmentById() throws Exception {
        ServiceAppointmentDTO appointmentDTO = new ServiceAppointmentDTO();
        appointmentDTO.setId(serviceAppointment.getId());
        appointmentDTO.setClientName(serviceAppointment.getClientName());

        when(serviceAppointmentService.findById(1L)).thenReturn(Optional.of(serviceAppointment));
        when(serviceAppointmentService.convertToDTO(serviceAppointment)).thenReturn(appointmentDTO);

        mockMvc.perform(get("/api/appointments/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(appointmentDTO.getId()))
                .andExpect(jsonPath("$.clientName").value(appointmentDTO.getClientName()));
    }

    @Test
    public void testFindByBarberId() throws Exception {
        Long barberId = 1L;
        List<ServiceAppointment> appointments = new ArrayList<>();
        ServiceAppointment appointment1 = new ServiceAppointment();
        appointment1.setId(1L);
        appointment1.setClientName("John Doe");
        appointment1.setBarber(new Barber());
        appointments.add(appointment1);

        ServiceAppointment appointment2 = new ServiceAppointment();
        appointment2.setId(2L);
        appointment2.setClientName("Jane Smith");
        appointment2.setBarber(new Barber());
        appointments.add(appointment2);

        when(serviceAppointmentService.findByBarberId(barberId)).thenReturn(appointments);

        mockMvc.perform(get("/api/appointments/barber/{barberId}", barberId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].clientName").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].clientName").value("Jane Smith"));

        verify(serviceAppointmentService).findByBarberId(barberId);
    }

    @Test
    public void testFindByStatus() throws Exception {
        ServiceAppointment appointment1 = new ServiceAppointment();
        appointment1.setId(1L);
        appointment1.setClientName("Cliente Teste A");
        appointment1.setStatus(ServiceAppointmentStatus.CONFIRMADO);

        ServiceAppointment appointment2 = new ServiceAppointment();
        appointment2.setId(2L);
        appointment2.setClientName("Cliente Teste B");
        appointment2.setStatus(ServiceAppointmentStatus.CONFIRMADO);

        List<ServiceAppointment> appointments = Arrays.asList(appointment1, appointment2);

        when(serviceAppointmentService.findByStatus(ServiceAppointmentStatus.CONFIRMADO))
                .thenReturn(appointments);

        mockMvc.perform(get("/api/appointments/status/CONFIRMADO")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(appointments.size()))
                .andExpect(jsonPath("$[0].id").value(appointment1.getId()))
                .andExpect(jsonPath("$[0].clientName").value(appointment1.getClientName()))
                .andExpect(jsonPath("$[0].status").value(appointment1.getStatus().toString()))
                .andExpect(jsonPath("$[1].id").value(appointment2.getId()))
                .andExpect(jsonPath("$[1].clientName").value(appointment2.getClientName()))
                .andExpect(jsonPath("$[1].status").value(appointment2.getStatus().toString()));
    }

    @Test
    public void testDeleteAppointment() throws Exception {
        doNothing().when(serviceAppointmentService).deleteById(1L);
        when(serviceAppointmentService.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/appointments/1"))
                .andExpect(status().isNoContent());
    }
}