package br.org.unicortes.barbearia.dtos;

import br.org.unicortes.barbearia.enums.EstoqueStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EstoqueDTO {
    private Long id;
    private Long produto;
    private Integer quantidade;
    private EstoqueStatus status;
    private BigDecimal precoCusto;
    private Integer limiteEstoqueBaixo;
}
