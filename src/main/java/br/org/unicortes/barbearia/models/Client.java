package br.org.unicortes.barbearia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tbClients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @SequenceGenerator(name = "client_seq", sequenceName = "client_sequence", allocationSize = 1)
    @Column(name = "client_id")
    private long id;

    @NotBlank(message = "O campo 'birthday' é obrigatório")
    @Column(name = "client_birthday")
    private LocalDate birthday;

    @NotBlank(message = "O campo 'email' é obrigatório")
    @Column(name = "client_email")
    private String email;
}
