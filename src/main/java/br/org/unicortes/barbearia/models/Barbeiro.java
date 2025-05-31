package br.org.unicortes.barbearia.models;

import br.org.unicortes.barbearia.dtos.BarbeiroDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_barbeiros")
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve conter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email é obrigatório")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Column(nullable = false, length = 20)
    private String telefone;

    @NotBlank(message = "CPF é obrigatório")
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @NotNull(message = "Salário é obrigatório")
    @PositiveOrZero(message = "Salário não pode ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salario;

    @NotBlank(message = "Endereço é obrigatório")
    @Column(nullable = false, length = 200)
    private String endereco;

    @NotNull(message = "Data de admissão é obrigatória")
    @PastOrPresent(message = "Data de admissão deve ser atual ou passada")
    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(name = "data_demissao")
    private LocalDate dataDemissao;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private boolean ativo = true;

    @Column(name = "especialidades", length = 500)
    private String especialidades;

    @OneToMany(mappedBy = "barbeiro", cascade = CascadeType.ALL)
    private List<HorarioLivre> horariosDisponiveis;

    @OneToMany(mappedBy = "barbeiro")
    private List<Agendamento> agendamentos;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
}
