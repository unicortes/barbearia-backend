package br.org.unicortes.barbearia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @SequenceGenerator(name = "client_seq", sequenceName = "client_sequence", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "O campo 'name' é obrigatório")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "O telefone é obrigatório")
    @Column(name = "phone")
    private String phone;

    @NotNull(message = "O campo 'birthday' é obrigatório")
    @Column(name = "birthday")
    private LocalDate birthday;

    @NotBlank(message = "O campo 'email' é obrigatório")
    @Column(name = "email")
    private String email;
}