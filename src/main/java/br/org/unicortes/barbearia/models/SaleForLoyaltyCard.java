package br.org.unicortes.barbearia.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbSaleForLoyaltyCards")
public class SaleForLoyaltyCard extends Sale{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "loyalty_card_id", nullable = false)
    private LoyaltyCard loyaltyCard;


}
