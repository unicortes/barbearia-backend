package br.org.unicortes.barbearia.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Produto com o id '" + id + "' não encontrado.");
    }
}
