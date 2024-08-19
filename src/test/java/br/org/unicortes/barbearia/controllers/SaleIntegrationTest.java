package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.Sale;
import br.org.unicortes.barbearia.repositories.SaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SaleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        saleRepository.deleteAll();
    }

    @Test
    void testCreateSale() throws Exception {
        Date date = dateFormat.parse("2024-09-24");

        Sale sale = new Sale();
        sale.setSaleName("Aniversário");
        sale.setSaleDescription("Promoção de aniversário do cliente");
        sale.setSalePromoCode("B1RTHD4Y");
        sale.setSaleDiscount(0.2);
        sale.setSaleExpirationDate(date);
        sale.setSaleCategory("Promoção");
        sale.setSaleAvailability(true);

        mockMvc.perform(post("/api/sales/newSale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sale)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saleName").value("Aniversário"));
    }

    @Test
    void testGetAllSales() throws Exception {
        Date date = dateFormat.parse("2024-09-24");

        Sale sale = new Sale();
        sale.setSaleName("Aniversário");
        sale.setSaleDescription("Promoção de aniversário do cliente");
        sale.setSalePromoCode("B1RTHD4Y");
        sale.setSaleDiscount(0.2);
        sale.setSaleExpirationDate(date);
        sale.setSaleCategory("Promoção");
        sale.setSaleAvailability(true);
        saleRepository.save(sale);

        mockMvc.perform(get("/api/sales/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].saleName").value("Aniversário"));
    }

    @Test
    void testGetSaleById() throws Exception {
        Date date = dateFormat.parse("2024-09-24");

        Sale sale = new Sale();
        sale.setSaleName("Aniversário");
        sale.setSaleDescription("Promoção de aniversário do cliente");
        sale.setSalePromoCode("B1RTHD4Y");
        sale.setSaleDiscount(0.2);
        sale.setSaleExpirationDate(date);
        sale.setSaleCategory("Promoção");
        sale.setSaleAvailability(true);
        Sale savedSale = saleRepository.save(sale);

        mockMvc.perform(get("/api/sales/sale/{id}", savedSale.getSaleId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saleName").value("Aniversário"));
    }

    @Test
    void testUpdateSale() throws Exception {
        Date date = dateFormat.parse("2024-09-24");

        Sale sale = new Sale();
        sale.setSaleName("Aniversário");
        sale.setSaleDescription("Promoção de aniversário do cliente");
        sale.setSalePromoCode("B1RTHD4Y");
        sale.setSaleDiscount(0.2);
        sale.setSaleExpirationDate(date);
        sale.setSaleCategory("Promoção");
        sale.setSaleAvailability(true);
        Sale savedSale = saleRepository.save(sale);

        Sale updatedSale = new Sale();
        updatedSale.setSaleName("Condicionador");
        updatedSale.setSaleDescription("Promoção de condicionador");
        updatedSale.setSalePromoCode("CONDI2024");
        updatedSale.setSaleDiscount(0.15);
        updatedSale.setSaleExpirationDate(date);
        updatedSale.setSaleCategory("Promoção");
        updatedSale.setSaleAvailability(false);

        mockMvc.perform(put("/api/sales/sale/edit/{id}", savedSale.getSaleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSale)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saleName").value("Condicionador"));
    }

    @Test
    void testDeleteSale() throws Exception {
        Date date = dateFormat.parse("2024-09-24");

        Sale sale = new Sale();
        sale.setSaleName("Aniversário");
        sale.setSaleDescription("Promoção de aniversário do cliente");
        sale.setSalePromoCode("B1RTHD4Y");
        sale.setSaleDiscount(0.2);
        sale.setSaleExpirationDate(date);
        sale.setSaleCategory("Promoção");
        sale.setSaleAvailability(true);
        Sale savedSale = saleRepository.save(sale);

        mockMvc.perform(delete("/api/sales/sale/delete/{id}", savedSale.getSaleId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/sales/sale/{id}", savedSale.getSaleId()))
                .andExpect(status().isNotFound());
    }
}
