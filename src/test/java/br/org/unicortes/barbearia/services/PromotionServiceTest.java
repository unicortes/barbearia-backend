package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.PromotionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.org.unicortes.barbearia.models.Promotion;
import br.org.unicortes.barbearia.repositories.PromotionRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceTest {
    @InjectMocks
    private PromotionService promotionService;

    @Mock
    private PromotionRepository promotionRepository;

    private Promotion promotion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        promotion = new Promotion();
        promotion.setId(1L);
        promotion.setName("Nova Promoção");
        promotion.setDescription("Descrição 1");
        promotion.setPromotionCode("PROMO");
        promotion.setCategory("Categoria 1");
        promotion.setDiscount(10.0);
        promotion.setAvailability(true);
        promotion.setStartDate(LocalDate.of(2024, 7, 1));
        promotion.setEndDate(LocalDate.of(2024, 7, 31));
    }

    @Test
    void testCreatePromotion() {
        when(promotionRepository.save(promotion)).thenReturn(promotion);
        Promotion createdPromotion = promotionService.createPromotion(promotion);

        assertNotNull(createdPromotion);
        assertEquals(createdPromotion.getId(), promotion.getId());
        verify(promotionRepository, times(1)).save(promotion);
    }

    @Test
    void testUpdatePromotion() {
        Long id = 1L;
        when(promotionRepository.findById(id)).thenReturn(Optional.of(promotion));
        when(promotionRepository.save(promotion)).thenReturn(promotion);
        Promotion updatedPromotion = promotionService.updatePromotion(id, promotion);

        assertNotNull(updatedPromotion);
        assertEquals(promotion.getId(), promotion.getId());
        verify(promotionRepository, times(1)).findById(id);
        verify(promotionRepository, times(1)).save(promotion);
    }

    @Test
    void testDeletePromotion() {
        Long id = 1L;
        when(promotionRepository.findById(id)).thenReturn(Optional.of(promotion));
        doNothing().when(promotionRepository).delete(promotion);

        assertDoesNotThrow(() -> promotionService.deletePromotion(id));
        verify(promotionRepository, times(1)).delete(promotion);
    }

    @Test
    void testGetAllPromotions() {
        when(promotionRepository.findAll()).thenReturn(Arrays.asList(promotion));
        List<Promotion> promotions = promotionService.getAllPromotions();

        assertNotNull(promotions);
        assertEquals(promotions.size(), 1);
        verify(promotionRepository, times(1)).findAll();
    }

    @Test
    void testUpdatePromotionThrowsExceptionWhenNotFound() {
        Long id = 1L;
        when(promotionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PromotionNotFoundException.class, () -> promotionService.updatePromotion(id, promotion));
        verify(promotionRepository, times(1)).findById(id);
        verify(promotionRepository, times(0)).save(promotion);
    }
}