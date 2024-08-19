package br.org.unicortes.barbearia.dtos;

import br.org.unicortes.barbearia.models.LoyaltyCard;
import lombok.Data;

@Data
public class SaleForLoyaltyCardDTO extends SaleDTO{

    private LoyaltyCard loyaltyCard;


}
