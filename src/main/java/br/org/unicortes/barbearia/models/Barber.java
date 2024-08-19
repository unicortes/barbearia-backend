package br.org.unicortes.barbearia.models;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "tb_barber")
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long barberId;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Email(message = "O e-mail deve ser válido")
    @NotBlank(message = "O e-mail é obrigatório")
    private String barberEmail;

    @NotBlank(message = "O telefone é obrigatório")
    private String barberTelefone;

    @NotBlank(message = "O CPF é obrigatório")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos")
    private String barberCpf;

    @NotNull(message = "O salário é obrigatório")
    @PositiveOrZero(message = "O salário deve ser um valor positivo ou zero")
    private Double barberSalario;

    @NotBlank(message = "O endereço é obrigatório")
    private String barberEndereco;

    @NotNull(message = "A data de admissão é obrigatória")
    private LocalDate barberDataDeAdimissao;

    @NotBlank(message = "Os horários de atendimento são obrigatórios")
    private String barberHorariosAtendimento;
}
