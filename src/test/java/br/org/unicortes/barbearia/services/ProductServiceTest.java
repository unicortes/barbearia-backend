package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Product;
import br.org.unicortes.barbearia.exceptions.ProductAlreadyExistsException;
import br.org.unicortes.barbearia.exceptions.ProductNotFoundException;
import br.org.unicortes.barbearia.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("Shampoo");
        product.setDescription("Hair Shampoo");
        product.setCategory("Hygiene");
        product.setExpirationDate(new java.util.Date());
        product.setCost(10.99);
        product.setType("Liquid");
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.getAllProducts();

        assertEquals(1, products.size());
        assertEquals("Shampoo", products.get(0).getName());
    }

    @Test
    void testGetProductByIdSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals("Shampoo", foundProduct.getName());
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void testCreateProductSuccess() {
        when(productRepository.existsByName("Shampoo")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.createProduct(product);

        assertNotNull(savedProduct);
        assertEquals("Shampoo", savedProduct.getName());
    }

    @Test
    void testCreateProductAlreadyExists() {
        when(productRepository.existsByName("Shampoo")).thenReturn(true);
        assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(product));
    }
}
