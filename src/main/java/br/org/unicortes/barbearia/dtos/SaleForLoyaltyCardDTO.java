package br.org.unicortes.barbearia.dtos;

import br.org.unicortes.barbearia.models.LoyaltyCard;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class SaleForLoyaltyCardDTO extends SaleDTO{

    private LoyaltyCard loyaltyCard;

}
