package br.org.unicortes.barbearia.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartaoFidelidadeDTO {
    private Long id;
    private Long clienteId;
    private Long servicoId;
    private LocalDate dataAdesao;
    private Integer pontos;
    private Boolean ativo;
    private LocalDate dataUltimaAtualizacao;
}
