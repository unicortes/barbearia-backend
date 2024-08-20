package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Client;
import br.org.unicortes.barbearia.models.LoyaltyCard;
import br.org.unicortes.barbearia.models.SaleForLoyaltyCard;
import br.org.unicortes.barbearia.repositories.LoyaltyCardRepository;
import br.org.unicortes.barbearia.repositories.SaleForLoyaltyCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoyaltyCardServiceTest {
    @Mock
    private LoyaltyCardRepository loyaltyCardRepository;

    @Mock
    private SaleForLoyaltyCardRepository saleForLoyaltyCardRepository;

    @InjectMocks
    private LoyaltyCardService loyaltyCardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLoyaltyCard() {
        LoyaltyCard loyaltyCard = new LoyaltyCard();
        when(loyaltyCardRepository.save(any(LoyaltyCard.class))).thenReturn(loyaltyCard);

        LoyaltyCard result = loyaltyCardService.createLoyaltyCard(loyaltyCard);

        assertEquals(loyaltyCard, result);
        verify(loyaltyCardRepository, times(1)).save(loyaltyCard);
    }

    @Test
    void testGetLoyaltyCard() {
        LoyaltyCard loyaltyCard = new LoyaltyCard();
        when(loyaltyCardRepository.findById(1L)).thenReturn(Optional.of(loyaltyCard));

        LoyaltyCard result = loyaltyCardService.getLoyaltyCard(1L);

        assertEquals(loyaltyCard, result);
        verify(loyaltyCardRepository, times(1)).findById(1L);
    }


    @Test
    void testDeleteLoyaltyCard() {
        doNothing().when(loyaltyCardRepository).deleteById(anyLong());

        loyaltyCardService.deleteLoyaltyCard(1L);

        verify(loyaltyCardRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCreateSaleForLoyaltyCard() {
        SaleForLoyaltyCard saleForLoyaltyCard = new SaleForLoyaltyCard();
        LoyaltyCard loyaltyCard = new LoyaltyCard();
        when(saleForLoyaltyCardRepository.save(any(SaleForLoyaltyCard.class))).thenReturn(saleForLoyaltyCard);

        SaleForLoyaltyCard result = loyaltyCardService.createSaleForLoyaltyCard(saleForLoyaltyCard, loyaltyCard);

        assertEquals(saleForLoyaltyCard, result);
        verify(saleForLoyaltyCardRepository, times(1)).save(saleForLoyaltyCard);
    }

    @Test
    void testCreateBirthdaySale() {
        Client client = new Client();
        client.setId(1L);
        client.setBirthday(LocalDate.now());
        SaleForLoyaltyCard saleForLoyaltyCard = new SaleForLoyaltyCard();
        LoyaltyCard loyaltyCard = new LoyaltyCard();

        when(loyaltyCardRepository.findByClientId((long) anyInt())).thenReturn(loyaltyCard);
        when(saleForLoyaltyCardRepository.save(any(SaleForLoyaltyCard.class))).thenReturn(saleForLoyaltyCard);

        loyaltyCardService.createBirthdaySale(client, saleForLoyaltyCard);

        verify(saleForLoyaltyCardRepository, times(1)).save(saleForLoyaltyCard);
    }
}
