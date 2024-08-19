package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Product;
import br.org.unicortes.barbearia.models.Sale;
import br.org.unicortes.barbearia.repositories.ProductRepository;
import br.org.unicortes.barbearia.repositories.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private SaleService saleService;

    private Sale sale;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        sale = new Sale();
        Date date = new Date();

        sale.setSaleName("Aniversário");
        sale.setSaleDescription("Promoção de aniversário do cliente");
        sale.setSalePromoCode("B1RTHD4Y");
        sale.setSaleDiscount(0.2);
        sale.setSaleExpirationDate(date);
        sale.setSaleCategory("Promoção");
        sale.setSaleAvailability(true);

    }

    @Test
    public void testGetSaleById() {
        Long saleId = 1L;
        sale.setSaleId(saleId);

        when(saleRepository.findBySaleId(saleId)).thenReturn(sale);

        Sale result = saleService.getSaleById(saleId);

        assertNotNull(result);
        assertEquals(saleId, result.getSaleId());
        verify(saleRepository, times(1)).findBySaleId(saleId);
    }

    @Test
    public void testGetSaleByIdNonExisting() {
        Long saleId = 2L;

        when(saleRepository.findBySaleId(saleId)).thenReturn(null);

        Sale result = saleService.getSaleById(saleId);

        assertNull(result);

        verify(saleRepository, times(1)).findBySaleId(saleId);
    }


    @Test
    public void testCreateSale() {
        sale.setSaleId(1L);

        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        Sale result = saleService.createSale(sale);

        assertNotNull(result);
        assertEquals(1L, result.getSaleId());
        verify(saleRepository, times(1)).save(sale);
    }

    @Test
    public void testUpdateSale() {
        sale.setSaleId(1L);

        when(saleRepository.existsById(sale.getSaleId())).thenReturn(true);
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        Sale result = saleService.updateSale(sale);

        assertNotNull(result);
        assertEquals(1L, result.getSaleId());
        verify(saleRepository, times(1)).existsById(sale.getSaleId());
        verify(saleRepository, times(1)).save(sale);
    }

    @Test
    public void testDeleteSale() {
        Long saleId = 1L;

        doNothing().when(saleRepository).deleteById(saleId);

        saleService.deleteSale(saleId);

        verify(saleRepository, times(1)).deleteById(saleId);
    }
}
