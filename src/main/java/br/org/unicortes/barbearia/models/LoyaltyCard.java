package br.org.unicortes.barbearia.models;

import jakarta.persistence.*;
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
    private Client clientId;

    @Column(name = "date_admission", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateAdmission;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Servico service;

    @Column(name = "points")
    private int points;
}
