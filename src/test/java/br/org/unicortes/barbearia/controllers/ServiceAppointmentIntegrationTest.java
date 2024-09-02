package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.ServiceAppointmentDTO;
import br.org.unicortes.barbearia.enums.ServiceAppointmentStatus;
import br.org.unicortes.barbearia.models.AvailableTime;
import br.org.unicortes.barbearia.models.Barber;
import br.org.unicortes.barbearia.models.ServiceAppointment;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.ServiceAppointmentRepository;
import br.org.unicortes.barbearia.services.AuthService;
import br.org.unicortes.barbearia.services.ServiceAppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.reflect.Array.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ServiceAppointmentController.class)
@Import(ServiceAppointmentIntegrationTest.TestSecurityConfig.class)
public class ServiceAppointmentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceAppointmentService serviceAppointmentService;

    @MockBean
    private ServiceAppointmentRepository serviceAppointmentRepository;

    @MockBean
    private AuthService authService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ServiceAppointment serviceAppointment;
    private ServiceAppointmentDTO serviceAppointmentDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

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

    private ResultActions performAuthenticatedRequest(HttpMethod method, String url, Object content, ResultMatcher status) throws Exception {
        var requestBuilder = MockMvcRequestBuilders
                .request(method, url)
                .contentType(MediaType.APPLICATION_JSON);

        if (content != null) {
            String json = objectMapper.writeValueAsString(content);
            requestBuilder.content(json);
        }

        return mockMvc.perform(requestBuilder.with(csrf()))
                .andExpect(status);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    public void testGetAllAppointments() throws Exception {
        List<ServiceAppointment> appointments = Arrays.asList(serviceAppointment);
        when(serviceAppointmentService.findAll()).thenReturn(appointments);
        when(serviceAppointmentService.convertToDTO(serviceAppointment)).thenReturn(serviceAppointmentDTO);

        MvcResult result = performAuthenticatedRequest(HttpMethod.GET, "/api/appointments", null, status().isOk())
                .andExpect(jsonPath("$[0].id").value(serviceAppointmentDTO.getId()))
                .andExpect(jsonPath("$[0].clientName").value(serviceAppointmentDTO.getClientName()))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    public void testGetAppointmentById() throws Exception {
        when(serviceAppointmentService.findById(1L)).thenReturn(Optional.of(serviceAppointment));
        when(serviceAppointmentService.convertToDTO(serviceAppointment)).thenReturn(serviceAppointmentDTO);

        MvcResult result = performAuthenticatedRequest(HttpMethod.GET, "/api/appointments/1", null, status().isOk())
                .andExpect(jsonPath("$.id").value(serviceAppointmentDTO.getId()))
                .andExpect(jsonPath("$.clientName").value(serviceAppointmentDTO.getClientName()))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    public void testFindByBarberId() throws Exception {
        Long barberId = 1L;
        List<ServiceAppointment> appointments = Arrays.asList(serviceAppointment);

        when(serviceAppointmentService.findByBarberId(barberId)).thenReturn(appointments);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/barber/{barberId}", barberId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(serviceAppointment.getId()))
                .andExpect(jsonPath("$[0].clientName").value(serviceAppointment.getClientName()))
                .andReturn();

        verify(serviceAppointmentService).findByBarberId(barberId);
        assertEquals(200, result.getResponse().getStatus());
    }


    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    public void testFindByStatus() throws Exception {
        List<ServiceAppointment> appointments = Arrays.asList(serviceAppointment);
        when(serviceAppointmentService.findByStatus(ServiceAppointmentStatus.PENDENTE)).thenReturn(appointments);

        MvcResult result = performAuthenticatedRequest(HttpMethod.GET, "/api/appointments/status/PENDENTE", null, status().isOk())
                .andExpect(jsonPath("$.length()").value(appointments.size()))
                .andExpect(jsonPath("$[0].id").value(serviceAppointment.getId()))
                .andExpect(jsonPath("$[0].clientName").value(serviceAppointment.getClientName()))
                .andExpect(jsonPath("$[0].status").value(ServiceAppointmentStatus.PENDENTE.toString()))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    public void testDeleteAppointment() throws Exception {
        doNothing().when(serviceAppointmentService).deleteById(1L);
        when(serviceAppointmentService.existsById(1L)).thenReturn(true);

        performAuthenticatedRequest(HttpMethod.DELETE, "/api/appointments/1", null, status().isNoContent());
    }

    @Test
    @WithMockUser(username = "barberUser", roles = "BARBER")
    public void testFindAppointmentsByDateAfter() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(1);
        List<ServiceAppointment> appointments = Arrays.asList(serviceAppointment);

        when(serviceAppointmentService.findByAppointmentDateTimeAfter(dateTime)).thenReturn(appointments);

        MvcResult result = performAuthenticatedRequest(HttpMethod.GET, "/api/appointments/after/" + dateTime.toString(), null, status().isOk())
                .andExpect(jsonPath("$[0].id").value(serviceAppointment.getId()))
                .andExpect(jsonPath("$[0].clientName").value(serviceAppointment.getClientName()))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "barberUser", roles = "BARBER")
    public void testFindAppointmentsBetweenDates() throws Exception {
        LocalDateTime start = LocalDateTime.now().minusDays(5);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        List<ServiceAppointment> appointments = Arrays.asList(serviceAppointment);

        when(serviceAppointmentService.findByAppointmentDateTimeBetween(start, end)).thenReturn(appointments);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/between")
                        .param("start", start.toString())
                        .param("end", end.toString())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(serviceAppointment.getId()))
                .andExpect(jsonPath("$[0].clientName").value(serviceAppointment.getClientName()))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "barberUser", roles = "BARBER")
    public void testUpdateAppointmentStatus() throws Exception {

        when(serviceAppointmentService.updateAppointmentStatus(1L, ServiceAppointmentStatus.CONFIRMADO))
                .thenReturn(serviceAppointment);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/appointments/1/status")
                        .param("status", ServiceAppointmentStatus.CONFIRMADO.toString())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }


    @Test
    @WithMockUser(username = "barberUser", roles = "BARBER")
    public void testGetDailySchedule() throws Exception {
        LocalDateTime date = LocalDateTime.now();
        List<ServiceAppointment> appointments = Arrays.asList(serviceAppointment);
        List<ServiceAppointmentDTO> appointmentDTOs = appointments.stream()
                .map(serviceAppointmentService::convertToDTO)
                .collect(Collectors.toList());

        when(serviceAppointmentService.findAppointmentsByBarberAndDate(1L, date)).thenReturn(appointments);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/barber/1/daily-schedule")
                        .param("date", date.toString())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "barberUser", roles = "BARBER")
    public void testGetAppointmentsByBarberAndDateTimeRange() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(1);
        List<ServiceAppointment> appointments = Arrays.asList(serviceAppointment);

        when(serviceAppointmentService.getAppointmentsByBarberAndDateTimeRange(1L, startDateTime, endDateTime))
                .thenReturn(appointments);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/barber/timeRange/1")
                        .param("startDateTime", startDateTime.toString())
                        .param("endDateTime", endDateTime.toString())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(serviceAppointment.getId()))
                .andExpect(jsonPath("$[0].clientName").value(serviceAppointment.getClientName()))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "BARBER")
    public void testUpdateAppointmentStatusWhenAppointmentNotFound() throws Exception {
        ServiceAppointmentStatus newStatus = ServiceAppointmentStatus.CONFIRMADO;

        when(serviceAppointmentService.updateAppointmentStatus(1L, newStatus)).thenThrow(new IllegalArgumentException("Appointment not found"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/appointments/{id}/status", 1L)
                        .param("status", newStatus.toString())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void testGetAppointmentsByBarberAndDateTimeRangeService() {
        Long barberId = 1L;
        LocalDateTime startDateTime = LocalDateTime.of(2024, 9, 1, 8, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 9, 1, 12, 0);

        ServiceAppointment serviceAppointment = new ServiceAppointment();
        serviceAppointment.setId(1L);
        serviceAppointment.setClientName("Teste 1");

        List<ServiceAppointment> appointments = Arrays.asList(serviceAppointment);

        when(serviceAppointmentService.getAppointmentsByBarberAndDateTimeRange(barberId, startDateTime, endDateTime))
                .thenReturn(appointments);

        List<ServiceAppointment> result = serviceAppointmentService.getAppointmentsByBarberAndDateTimeRange(barberId, startDateTime, endDateTime);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Teste 1", result.get(0).getClientName());
    }

    @EnableWebSecurity
    static class TestSecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
            return http.build();
        }
    }
}
