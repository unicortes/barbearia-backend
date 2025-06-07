package br.org.unicortes.barbearia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String categoria;
    private BigDecimal precoCusto;
    private BigDecimal precoVenda;
    private Integer quantidadeEstoque;
    private LocalDate dataValidade;
    private boolean ativo;
}
