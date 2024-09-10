package br.org.unicortes.barbearia.exceptions;

public class PromotionAlreadyExistsException extends RuntimeException {
    public PromotionAlreadyExistsException(String name) {
        super("A promoção '" + name + "' já existe.");
    }
}