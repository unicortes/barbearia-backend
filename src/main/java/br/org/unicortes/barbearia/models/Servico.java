package br.org.unicortes.barbearia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do serviço é obrigatório")
    @Size(min = 3, max = 50, message = "Nome deve conter entre 3 e 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String nome;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    @Column(nullable = false, length = 255)
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "duracao_padrao_minutos", nullable = false)
    @Min(value = 15, message = "Duração mínima de 15 minutos")
    @Builder.Default
    private int duracaoPadraoMinutos = 30;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private boolean ativo = true;
}
