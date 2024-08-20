package br.org.unicortes.barbearia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tb_loyaltyCards")
public class LoyaltyCard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loyalty_card_seq")
    @SequenceGenerator(name = "loyalty_card_seq", sequenceName = "loyalty_card_sequence", allocationSize = 1)
    @Column(name = "loyalty_card_id")
    private Long id;

    @NotBlank(message = "O campo 'cliente' é obrigatório")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @NotBlank(message = "O campo 'data de admissão' é obrigatório")
    @Column(name = "admission_date")
    private Date admissionDate;

    @Size(min = 1, message = "É necessário no mínimo 1 serviço para estar incluído no Cartão Fidelidade")
    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "services_aquired")
    private List<Servico> servicesAquired;
}
