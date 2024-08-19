package br.org.unicortes.barbearia.dtos;

import br.org.unicortes.barbearia.models.Client;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.util.Date;

public class LoyaltyCardDTO {

    private long id;

    private Client client;

    private Date admissionDate;

    //private List<Service> servicesAquired;
}
