package br.org.unicortes.barbearia.exceptions;

public class SaleForLoyaltyCardNotFoundException extends RuntimeException {
    public SaleForLoyaltyCardNotFoundException (Long id) {
        super("Venda por cartão fidelidade com o id '" + id + "' não encontrado.");
    }
}