package br.org.unicortes.barbearia.models;

import br.org.unicortes.barbearia.dtos.CartaoFidelidadeDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_cartoes_fidelidade")
public class CartaoFidelidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "Serviço é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @NotNull(message = "Data de adesão é obrigatória")
    @PastOrPresent(message = "Data deve ser atual ou passada")
    @Column(name = "data_adesao", nullable = false)
    private LocalDate dataAdesao;

    @NotNull(message = "Pontos são obrigatórios")
    @Min(value = 0, message = "Pontos não podem ser negativos")
    @Column(nullable = false)
    private Integer pontos;

    @NotNull(message = "Status é obrigatório")
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @Column(name = "data_ultima_atualizacao")
    private LocalDate dataUltimaAtualizacao;

    public boolean isPremiado() {
        return this.pontos >= 10;
    }
}
