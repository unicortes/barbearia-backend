package br.org.unicortes.barbearia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "tb_saleForLoyaltyCard")
public class SaleForLoyaltyCard extends Sale {
    @NotNull(message = "A promoção precisa estar associada a um cartão fidelidade")
    @OneToOne
    @JoinColumn(name = "loyalty_card_id", nullable = false)
    private LoyaltyCard loyaltyCard;
}
