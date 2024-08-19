package br.org.unicortes.barbearia.dtos;

import br.org.unicortes.barbearia.models.Client;
import br.org.unicortes.barbearia.models.Servico;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LoyaltyCardDTO {

    private long id;

    private Client client;

    private Date admissionDate;

    private List<Servico> servicesAquired;
}
