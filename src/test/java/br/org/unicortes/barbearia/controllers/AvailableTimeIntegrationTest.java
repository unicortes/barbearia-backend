package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.AvailableTime;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

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
    @WithMockUser(username = "testuser", roles = "ADMIN")
    void createAvailableTime() throws Exception {
        AvailableTime availableTime = new AvailableTime();
        availableTime.setId(1L);

        when(availableTimeService.create(any(AvailableTime.class))).thenReturn(availableTime);

        performAuthenticatedRequest(HttpMethod.POST, "/api/available-times", availableTime, MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    void getAvailableTimesByServiceId() throws Exception {
        AvailableTime time1 = new AvailableTime();
        AvailableTime time2 = new AvailableTime();
        List<AvailableTime> times = Arrays.asList(time1, time2);
        Long serviceId = 1L;

        when(availableTimeService.findByServiceId(serviceId)).thenReturn(times);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/available-times/service/{serviceId}", serviceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").isNotEmpty())
                .andReturn();

        assertNotNull(result.getResponse());
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    void getAllAvailableTimes() throws Exception {
        AvailableTime time1 = new AvailableTime();
        AvailableTime time2 = new AvailableTime();
        List<AvailableTime> times = Arrays.asList(time1, time2);

        when(availableTimeService.getAll()).thenReturn(times);

        performAuthenticatedRequest(HttpMethod.GET, "/api/available-times", null, MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    void deleteAvailableTime() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/available-times/{id}", id)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "ADMIN")
    void getUnscheduledTimes() throws Exception {
        AvailableTime time1 = new AvailableTime();
        AvailableTime time2 = new AvailableTime();
        List<AvailableTime> times = Arrays.asList(time1, time2);

        when(availableTimeService.findByIsScheduledFalse()).thenReturn(times);

        performAuthenticatedRequest(HttpMethod.GET, "/api/available-times/unscheduled", null, MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").isNotEmpty());
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
