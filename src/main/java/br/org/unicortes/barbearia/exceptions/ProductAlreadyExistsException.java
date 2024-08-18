package br.org.unicortes.barbearia.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String name) {
        super("O Produto '" + name + "' já existe.");
    }
}
