package br.org.unicortes.barbearia.models;

import br.org.unicortes.barbearia.enums.EstoqueStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Produto é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Produto produto;

    @PositiveOrZero(message = "Quantidade não pode ser negativa")
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull(message = "Status do estoque é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstoqueStatus status;

    @PositiveOrZero(message = "Preço de custo não pode ser negativo")
    @Column(precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @Column(name = "limite_estoque_baixo")
    private Integer limiteEstoqueBaixo;
}
