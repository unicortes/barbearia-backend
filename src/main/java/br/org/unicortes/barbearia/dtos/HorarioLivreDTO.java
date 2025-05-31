package br.org.unicortes.barbearia.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioLivreDTO {
    private Long id;
    private Long barbeiroId;
    private Long servicoId;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private boolean disponivel;
}
