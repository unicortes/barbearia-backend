package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.StockDTO;
import br.org.unicortes.barbearia.enums.StockStatus;
import br.org.unicortes.barbearia.models.Product;
import br.org.unicortes.barbearia.models.Stock;
import br.org.unicortes.barbearia.repositories.ProductRepository;
import br.org.unicortes.barbearia.repositories.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StockIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product product;
    private Stock stock;

    @BeforeEach
    public void setUp() {
        stockRepository.deleteAll();
        productRepository.deleteAll();

        product = new Product();
        product.setName("Shampoo");
        product.setDescription("Hair Shampoo");
        product.setCategory("Hygiene");
        product.setExpirationDate(new java.util.Date());
        product.setCost(10.99);
        product.setType("Liquid");
        product = productRepository.save(product);

        stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(10);
        stock.setStatus(StockStatus.EM_USO);
        stockRepository.save(stock);
    }

    @Test
    public void testGetAllStocks() throws Exception {
        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productId", is(product.getId().intValue())));
    }

    @Test
    public void testGetStockById() throws Exception {
        mockMvc.perform(get("/api/stocks/" + stock.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(product.getId().intValue())));
    }

    @Test
    public void testCreateStock() throws Exception {
        mockMvc.perform(post("/api/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":" + product.getId() + ",\"quantity\":15,\"status\":\"LACRADO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(15)))
                .andExpect(jsonPath("$.status", is("LACRADO")));
    }

    @Test
    public void testUpdateStock() throws Exception {
        Product updatedProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        StockDTO stockDTO = new StockDTO();
        stockDTO.setProductId(updatedProduct.getId());
        stockDTO.setQuantity(20);
        stockDTO.setStatus(StockStatus.LACRADO);

        mockMvc.perform(put("/api/stocks/" + stock.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":" + updatedProduct.getId() + ",\"quantity\":20,\"status\":\"LACRADO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(20)))
                .andExpect(jsonPath("$.status", is("LACRADO")));
    }

    @Test
    public void testDeleteStock() throws Exception {
        mockMvc.perform(delete("/api/stocks/" + stock.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetStocksByProductName() throws Exception {
        mockMvc.perform(get("/api/stocks/product/name/Shampoo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productId", is(product.getId().intValue())));
    }
}
