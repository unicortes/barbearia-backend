package br.org.unicortes.barbearia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Telefone é obrigatório")
    @Column(nullable = false, length = 20)
    private String telefone;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Column(nullable = false)
    private LocalDate nascimento;

    @NotBlank(message = "Email é obrigatório")
    @Column(nullable = false, length = 100)
    private String email;

    @Column(name = "criado_em", nullable = false, updatable = false)
    @Builder.Default
    private LocalDate criadoEm = LocalDate.now();

    @Column(name = "is_ativo", nullable = false)
    @Builder.Default
    private Boolean isAtivo = true;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
}
