package br.org.unicortes.barbearia.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tbLoyaltyCards")
public class LoyaltyCard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loyalty_card_seq")
    @SequenceGenerator(name = "loyalty_card_seq", sequenceName = "loyalty_card_sequence", allocationSize = 1)
    @Column(name = "loyalty_card_id")
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "admission_date")
    private Date admissionDate;


   /* @Column(name = "services_aquired")
    private List<Service> servicesAquired; */
}
