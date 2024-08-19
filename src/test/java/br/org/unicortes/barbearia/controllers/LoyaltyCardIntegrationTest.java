package br.org.unicortes.barbearia.controllers;


import br.org.unicortes.barbearia.dtos.LoyaltyCardDTO;
import br.org.unicortes.barbearia.models.Client;
import br.org.unicortes.barbearia.models.SaleForLoyaltyCard;
import br.org.unicortes.barbearia.models.LoyaltyCard;
import br.org.unicortes.barbearia.repositories.LoyaltyCardRepository;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LoyaltyCardIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoyaltyCardRepository loyaltyCardRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {loyaltyCardRepository.deleteAll();}

    @Test
    public void testCreateLoyaltyCard() throws Exception {
        Date date = new Date("2024-09-24");
        Client client = new Client();
        client.setId(1);
        LoyaltyCard loyaltyCard = new LoyaltyCard();
        loyaltyCard.setClient(client);
        loyaltyCard.setAdmissionDate(date);
        loyaltyCard.setServicesAquired(new ArrayList<>());
        loyaltyCard.setId(1);

        mockMvc.perform(post("/api/loyaltyCards/newLoyaltyCard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loyaltyCard)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testGetLoyaltyCardById() throws Exception {
        Date date = new Date("2024-09-24");
        Client client = new Client();
        client.setId(1);
        LoyaltyCard loyaltyCard = new LoyaltyCard();
        loyaltyCard.setClient(client);
        loyaltyCard.setAdmissionDate(date);
        loyaltyCard.setServicesAquired(new ArrayList<>());
        loyaltyCard.setId(1);

        mockMvc.perform(get("/api/loyaltyCards/{id}", loyaltyCard.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loyaltyCard.getId()))
                .andExpect(jsonPath("$.client").exists())
                .andExpect(jsonPath("$.admissionDate").exists())
                .andExpect(jsonPath("$.servicesAquired").exists());
    }

    @Test
    public void testUpdateLoyaltyCard() throws Exception {
        Date date = new Date("2024-09-24");
        Client client = new Client();
        client.setId(1);
        LoyaltyCard loyaltyCard = new LoyaltyCard();
        loyaltyCard.setClient(client);
        loyaltyCard.setAdmissionDate(date);
        loyaltyCard.setServicesAquired(new ArrayList<>());
        loyaltyCard.setId(1);
        LoyaltyCard updatedCard = new LoyaltyCard();
        updatedCard.setClient(new Client());
        updatedCard.setAdmissionDate(date);
        updatedCard.setServicesAquired(new ArrayList<>());

        mockMvc.perform(put("/api/loyaltyCards/{id}", loyaltyCard.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedCard)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loyaltyCard.getId()))
                .andExpect(jsonPath("$.client").exists())
                .andExpect(jsonPath("$.admissionDate").exists())
                .andExpect(jsonPath("$.servicesAquired").exists());
    }


    @Test
    public void testDeleteLoyaltyCard() throws Exception {
        Date date = new Date("2024-09-24");
        Client client = new Client();
        client.setId(1);
        LoyaltyCard loyaltyCard = new LoyaltyCard();
        loyaltyCard.setClient(client);
        loyaltyCard.setAdmissionDate(date);
        loyaltyCard.setServicesAquired(new ArrayList<>());
        loyaltyCard.setId(1);

        LoyaltyCard updatedCard = new LoyaltyCard();
        updatedCard.setClient(new Client());
        updatedCard.setAdmissionDate(date);
        updatedCard.setServicesAquired(new ArrayList<>());

        mockMvc.perform(delete("/api/loyaltyCards/{id}", loyaltyCard.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testCreateLoyaltySale() throws Exception {
        Date date = new Date("2024-09-24");
        Client client = new Client();
        client.setId(1);

        LoyaltyCard loyaltyCard = new LoyaltyCard();
        loyaltyCard.setClient(client);
        loyaltyCard.setAdmissionDate(date);
        loyaltyCard.setServicesAquired(new ArrayList<>());
        loyaltyCard.setId(1);

        SaleForLoyaltyCard saleForLoyaltyCard = new SaleForLoyaltyCard();
        saleForLoyaltyCard.setSaleName("Aniversário");
        saleForLoyaltyCard.setSaleDescription("Promoção de aniversário do cliente");
        saleForLoyaltyCard.setSalePromoCode("B1RTHD4Y");
        saleForLoyaltyCard.setSaleDiscount(0.2);
        saleForLoyaltyCard.setSaleExpirationDate(date);
        saleForLoyaltyCard.setSaleCategory("Promoção");
        saleForLoyaltyCard.setSaleAvailability(true);
        saleForLoyaltyCard.setId(1L);

        mockMvc.perform(post("/api/loyaltyCards/sale/newSaleForLoyalty/{id}", loyaltyCard.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleForLoyaltyCard)))
                .andExpect(status().isNoContent());
    }

}
