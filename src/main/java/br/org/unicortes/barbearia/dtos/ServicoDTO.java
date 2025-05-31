package br.org.unicortes.barbearia.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ServicoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int duracaoPadraoMinutos;
    private boolean ativo;
}
