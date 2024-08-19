package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.enums.StockStatus;
import br.org.unicortes.barbearia.exceptions.StockNotFoundException;
import br.org.unicortes.barbearia.models.Stock;
import br.org.unicortes.barbearia.models.Product;
import br.org.unicortes.barbearia.repositories.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockServiceTest {

    @InjectMocks
    private StockService stockService;

    @Mock
    private StockRepository stockRepository;

    private Stock stock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Product product = new Product();
        product.setId(1L);
        product.setName("Shampoo");
        product.setDescription("Hair Shampoo");
        product.setCategory("Hygiene");
        product.setExpirationDate(new java.util.Date());
        product.setCost(10.99);
        product.setType("Liquid");

        stock = new Stock();
        stock.setId(1L);
        stock.setProduct(product);
        stock.setQuantity(10);
        stock.setStatus(StockStatus.EM_USO);
    }

    @Test
    public void testGetStockById() {
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

        Stock found = stockService.getStockById(1L);

        assertNotNull(found);
        assertEquals(stock.getId(), found.getId());
    }

    @Test
    public void testGetStockByIdNotFound() {
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, () -> stockService.getStockById(1L));
    }

    @Test
    public void testCreateStock() {
        when(stockRepository.save(stock)).thenReturn(stock);

        Stock created = stockService.createStock(stock);

        assertNotNull(created);
        assertEquals(stock.getQuantity(), created.getQuantity());
    }

    @Test
    public void testUpdateStock() {
        Stock updatedStock = new Stock();
        updatedStock.setQuantity(20);
        updatedStock.setStatus(StockStatus.LACRADO);

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepository.save(stock)).thenReturn(stock);

        Stock updated = stockService.updateStock(1L, updatedStock);

        assertNotNull(updated);
        assertEquals(20, updated.getQuantity());
        assertEquals(StockStatus.LACRADO, updated.getStatus());
    }

    @Test
    public void testDeleteStock() {
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        doNothing().when(stockRepository).delete(stock);

        stockService.deleteStock(1L);

        verify(stockRepository, times(1)).delete(stock);
    }

    @Test
    public void testDeleteStockNotFound() {
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, () -> stockService.deleteStock(1L));
    }
}
