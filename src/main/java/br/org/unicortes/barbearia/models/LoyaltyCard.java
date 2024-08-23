package br.org.unicortes.barbearia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tb_loyalty_card")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @NotNull(message = "O campo 'data de admissão' é obrigatório")
    private Date dateAdmission;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Servico service;

    @NotNull(message = "O campo 'pontos' é obrigatório")
    private Integer points;
}
