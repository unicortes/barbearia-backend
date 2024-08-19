package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Product;
import br.org.unicortes.barbearia.exceptions.ProductAlreadyExistsException;
import br.org.unicortes.barbearia.exceptions.ProductNotFoundException;
import br.org.unicortes.barbearia.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product createProduct(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new ProductAlreadyExistsException(product.getName());
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setCategory(productDetails.getCategory());
        product.setExpirationDate(productDetails.getExpirationDate());
        product.setCost(productDetails.getCost());
        product.setType(productDetails.getType());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
