package br.org.unicortes.barbearia.exceptions;

public class PromotionNotFoundException extends RuntimeException {
    public PromotionNotFoundException(Long id) {
        super("Promoção com o id '" + id + "' não encontrado.");
    }
}