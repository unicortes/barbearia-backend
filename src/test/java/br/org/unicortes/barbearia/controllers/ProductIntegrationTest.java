package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.Product;
import br.org.unicortes.barbearia.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void testCreateProduct() throws Exception {
        Product product = new Product();
        product.setName("Shampoo");
        product.setDescription("Shampoo 200ml");
        product.setCategory("Cabelos");
        product.setExpirationDate(new Date());
        product.setCost(15.99);
        product.setType("Para cabelos secos");

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Shampoo"));
    }

    @Test
    void testGetAllProducts() throws Exception {
        Product product = new Product();
        product.setName("Shampoo");
        product.setDescription("Shampoo 200ml");
        product.setCategory("Cabelos");
        product.setExpirationDate(new Date());
        product.setCost(15.99);
        product.setType("Para cabelos secos");
        productRepository.save(product);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Shampoo"));
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = new Product();
        product.setName("Shampoo");
        product.setDescription("Shampoo 200ml");
        product.setCategory("Cabelos");
        product.setExpirationDate(new Date());
        product.setCost(15.99);
        product.setType("Para cabelos secos");
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(get("/api/products/{id}", savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Shampoo"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product product = new Product();
        product.setName("Shampoo");
        product.setDescription("Shampoo 200ml");
        product.setCategory("Cabelos");
        product.setExpirationDate(new Date());
        product.setCost(15.99);
        product.setType("Para cabelos secos");

        Product savedProduct = productRepository.save(product);

        Product updatedProduct = new Product();
        updatedProduct.setName("Condicionador");
        updatedProduct.setDescription("Condicionador 200ml");
        updatedProduct.setCategory("Cabelos");
        updatedProduct.setExpirationDate(new Date());
        updatedProduct.setCost(17.99);
        updatedProduct.setType("Para cabelos secos");

        mockMvc.perform(put("/api/products/{id}", savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Condicionador"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Product product = new Product();
        product.setName("Shampoo");
        product.setDescription("Shampoo 200ml");
        product.setCategory("Cabelos");
        product.setExpirationDate(new Date());
        product.setCost(15.99);
        product.setType("Para cabelos secos");
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(delete("/api/products/{id}", savedProduct.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/{id}", savedProduct.getId()))
                .andExpect(status().isNotFound());

        if (productRepository.findById(savedProduct.getId()).isPresent()) {
            fail("O produto não foi deletado corretamente.");
        }
    }
}
