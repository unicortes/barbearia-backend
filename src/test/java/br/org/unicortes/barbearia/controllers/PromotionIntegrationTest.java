package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.exceptions.PromotionNotFoundException;
import br.org.unicortes.barbearia.models.Promotion;
import br.org.unicortes.barbearia.repositories.PromotionRepository;
import br.org.unicortes.barbearia.services.PromotionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PromotionIntegrationTest {
    @Autowired
    private PromotionService promotionService;

    @Autowired
    private PromotionRepository promotionRepository;

    private Promotion promotion;

    @BeforeEach
    void setUp() {
        promotionRepository.deleteAll();
        promotion = new Promotion();
        promotion.setId(1L);
        promotion.setName("Nova Promoção");
        promotion.setDescription("Descrição 1");
        promotion.setPromotionCode("PROMO");
        promotion.setCategory("Categoria 1");
        promotion.setDiscount(10.0);
        promotion.setAvailability(false);
        promotion.setStartDate(LocalDate.of(2024, 7, 1));
        promotion.setEndDate(LocalDate.of(2024, 7, 31));
    }

    @Test
    public void testCreatePromotion() {
        Promotion savedPromotion = promotionService.createPromotion(promotion);
        assertNotNull(savedPromotion);
        assertNotNull(savedPromotion.getId());
        assertEquals(promotion.getName(), savedPromotion.getName());

        Optional<Promotion> foundPromotion = promotionRepository.findById(savedPromotion.getId());
        assertTrue(foundPromotion.isPresent());
    }

    @Test
    public void testUpdatePromotion() {
        Promotion savedPromotion = promotionService.createPromotion(promotion);
        savedPromotion.setName("p2");
        Promotion updatedPromotion = promotionService.updatePromotion(savedPromotion.getId(), savedPromotion);
        assertNotNull(updatedPromotion);
        assertEquals("p2", updatedPromotion.getName());

        Optional<Promotion> foundPromotion = promotionRepository.findById(updatedPromotion.getId());
        assertTrue(foundPromotion.isPresent());
        assertEquals("p2", foundPromotion.get().getName());
    }

    @Test
    public void testDeletePromotion() {
        Promotion savedPromotion = promotionService.createPromotion(promotion);
        promotionService.deletePromotion(savedPromotion.getId());
        Optional<Promotion> foundPromotion = promotionRepository.findById(savedPromotion.getId());
        assertFalse(foundPromotion.isPresent());
    }

    @Test
    public void testGetAllPromotions() {
        Promotion promotion1 = promotionService.createPromotion(promotion);
        Promotion promotion2 = new Promotion();
        promotion2.setName("Promoção 23");
        promotion2.setDescription("Descrição 1");
        promotion2.setPromotionCode("PROMO");
        promotion2.setCategory("Categoria 1");
        promotion2.setDiscount(10.0);
        promotion2.setAvailability(false);
        promotion2.setStartDate(LocalDate.of(2024, 7, 1));
        promotion2.setEndDate(LocalDate.of(2024, 7, 31));
        promotionService.createPromotion(promotion2);

        List<Promotion> promotions = promotionService.getAllPromotions();

        assertNotNull(promotions);
        assertEquals(2, promotions.size());
    }

    @Test
    public void testPromotionThrowsExceptionWhenNotFound() {
        Promotion nonExistentPromotion = new Promotion();
        nonExistentPromotion.setId(99L);

        assertThrows(PromotionNotFoundException.class, () -> promotionService.updatePromotion(nonExistentPromotion.getId(), nonExistentPromotion));
    }
}