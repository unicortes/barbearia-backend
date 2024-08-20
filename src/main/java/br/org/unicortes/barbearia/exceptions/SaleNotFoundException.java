package br.org.unicortes.barbearia.exceptions;

public class SaleNotFoundException extends RuntimeException {
    public SaleNotFoundException (Long id) {
        super("Venda com o id '" + id + "' não encontrada.");
    }
}