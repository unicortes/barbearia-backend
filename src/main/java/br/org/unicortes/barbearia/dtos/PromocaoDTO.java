package br.org.unicortes.barbearia.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class PromocaoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String codigoPromo;
    private String categoria;
    private double desconto;
    private boolean disponibilidade;
    private LocalDate inicio;
    private LocalDate fim;

    public boolean getDisponibilidade() {
        return this.disponibilidade;
    }
}
