package br.org.unicortes.barbearia.exceptions;

public class LoyaltyCardNotFoundException extends RuntimeException {
    public LoyaltyCardNotFoundException (Long id) {
        super("Cartão fidelidade com o id '" + id + "' não encontrado.");
    }
}