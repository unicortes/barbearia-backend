// src/test/java/br/org/unicortes/barbearia/controllers/LoyaltyCardControllerIntegrationTest.java

package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.LoyaltyCardDTO;
import br.org.unicortes.barbearia.models.Client;
import br.org.unicortes.barbearia.models.LoyaltyCard;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.services.LoyaltyCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoyaltyCardIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoyaltyCardService loyaltyCardService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoyaltyCardDTO loyaltyCardDTO;
    private LoyaltyCard loyaltyCard;

    @BeforeEach
    public void setUp() {
        // Inicializa os mocks e configura o MockMvc
        MockitoAnnotations.openMocks(this);

        // Cria objetos de teste para LoyaltyCardDTO e LoyaltyCard
        loyaltyCardDTO = new LoyaltyCardDTO();
        loyaltyCardDTO.setId(1L);
        loyaltyCardDTO.setClientId(1L);
        loyaltyCardDTO.setDateAdmission(new java.util.Date());
        loyaltyCardDTO.setServiceId(1L);
        loyaltyCardDTO.setPoints(100);

        Client client = new Client();
        client.setId(1L);

        Servico service = new Servico();
        service.setServicoId(1L);

        loyaltyCard = new LoyaltyCard();
        loyaltyCard.setId(1L);
        loyaltyCard.setClient(client);
        loyaltyCard.setDateAdmission(new java.util.Date());
        loyaltyCard.setService(service);
        loyaltyCard.setPoints(100);
    }

    @Test
    public void testGetAllLoyaltyCards() throws Exception {
        // Configura o comportamento esperado do serviço
        when(loyaltyCardService.getAllLoyaltyCards()).thenReturn(Arrays.asList(loyaltyCard));

        // Realiza a requisição e verifica a resposta
        mockMvc.perform(get("/api/loyalty-cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].clientId").value(1))
                .andExpect(jsonPath("$[0].serviceId").value(1))
                .andExpect(jsonPath("$[0].points").value(100));
    }

    @Test
    public void testGetLoyaltyCardById() throws Exception {
        // Configura o comportamento esperado do serviço
        when(loyaltyCardService.getLoyaltyCardById(anyLong())).thenReturn(loyaltyCard);

        // Realiza a requisição e verifica a resposta
        mockMvc.perform(get("/api/loyalty-cards/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.serviceId").value(1))
                .andExpect(jsonPath("$.points").value(100));
    }

    @Test
    public void testCreateLoyaltyCard() throws Exception {
        // Configura o comportamento esperado do serviço
        when(loyaltyCardService.createLoyaltyCard(any(LoyaltyCard.class))).thenReturn(loyaltyCard);

        // Realiza a requisição e verifica a resposta
        mockMvc.perform(post("/api/loyalty-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loyaltyCardDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.serviceId").value(1))
                .andExpect(jsonPath("$.points").value(100));
    }

    @Test
    public void testUpdateLoyaltyCard() throws Exception {
        // Configura o comportamento esperado do serviço
        when(loyaltyCardService.updateLoyaltyCard(anyLong(), any(LoyaltyCard.class))).thenReturn(loyaltyCard);

        // Realiza a requisição e verifica a resposta
        mockMvc.perform(put("/api/loyalty-cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loyaltyCardDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.serviceId").value(1))
                .andExpect(jsonPath("$.points").value(100));
    }

    @Test
    public void testDeleteLoyaltyCard() throws Exception {
        // Realiza a requisição de exclusão e verifica a resposta
        mockMvc.perform(delete("/api/loyalty-cards/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
