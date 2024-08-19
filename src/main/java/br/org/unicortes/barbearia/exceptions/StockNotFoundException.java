package br.org.unicortes.barbearia.exceptions;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(Long id) {
        super("Estoque com o id '" + id + "' não encontrado.");
    }
}
