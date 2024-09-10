package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.AvailableTimeDTO;
import br.org.unicortes.barbearia.services.AuthService;
import br.org.unicortes.barbearia.services.AvailableTimeService;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AvailableTimeController.class)
@Import(AvailableTimeIntegrationTest.TestSecurityConfig.class)
public class AvailableTimeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvailableTimeService availableTimeService;

    @MockBean
    private AuthService authService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ResultActions performAuthenticatedRequest(HttpMethod method, String url, Object content, ResultMatcher status) throws Exception {
        var requestBuilder = MockMvcRequestBuilders
                .request(method, url)
                .contentType(MediaType.APPLICATION_JSON);

        if (content != null) {
            String json = objectMapper.writeValueAsString(content);
            requestBuilder.content(json);
        }

        return mockMvc.perform(requestBuilder)
                .andExpect(status);
    }

    @Test
    @WithMockUser
    void createAvailableTime() throws Exception {
        AvailableTimeDTO availableTimeDTO = AvailableTimeDTO.builder()
                .id(1L)
                .barber(1L)
                .service(1L)
                .timeStart(new Date())
                .timeEnd(new Date())
                .isScheduled(false)
                .build();

        when(availableTimeService.create(any(AvailableTimeDTO.class))).thenReturn(availableTimeDTO);

        mockMvc.perform(post("/api/available-times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(availableTimeDTO)))
                .andExpect(status().isOk()) // Espera o status 200 OK
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    @WithMockUser
    void getAvailableTimesByServiceId() throws Exception {
        AvailableTimeDTO time1 = AvailableTimeDTO.builder().id(1L).build();
        AvailableTimeDTO time2 = AvailableTimeDTO.builder().id(2L).build();
        List<AvailableTimeDTO> times = Arrays.asList(time1, time2);

        Long serviceId = 1L;

        when(availableTimeService.findByServiceId(serviceId)).thenReturn(times);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/available-times/service/{serviceId}", serviceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response content: " + responseContent);

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser
    void getAllAvailableTimes() throws Exception {
        AvailableTimeDTO time1 = AvailableTimeDTO.builder().id(1L).build();
        AvailableTimeDTO time2 = AvailableTimeDTO.builder().id(2L).build();
        List<AvailableTimeDTO> times = Arrays.asList(time1, time2);

        when(availableTimeService.getAll()).thenReturn(times);

        performAuthenticatedRequest(HttpMethod.GET, "/api/available-times", null, status().isOk())
                .andExpect(jsonPath("$[0]").isNotEmpty())
                .andExpect(jsonPath("$[1]").isNotEmpty());
    }

    @Test
    @WithMockUser
    void deleteAvailableTime() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/available-times/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void getUnscheduledTimes() throws Exception {
        AvailableTimeDTO time1 = AvailableTimeDTO.builder().id(1L).build();
        AvailableTimeDTO time2 = AvailableTimeDTO.builder().id(2L).build();
        List<AvailableTimeDTO> times = Arrays.asList(time1, time2);

        when(availableTimeService.findByIsScheduledFalse()).thenReturn(times);

        performAuthenticatedRequest(HttpMethod.GET, "/api/available-times/scheduled/false", null, status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }


    @EnableWebSecurity
    static class TestSecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(authorize ->
                            authorize.anyRequest().authenticated()
                    );
            return http.build();
        }
    }
}
