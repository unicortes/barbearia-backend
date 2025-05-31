package br.org.unicortes.barbearia.dtos;

import br.org.unicortes.barbearia.enums.AgendamentoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendamentoDTO {
    private Long id;
    private Long servicoId;
    private Long barbeiroId;
    private Long clienteId;
    private Long horarioLivreId;
    private LocalDateTime dataHora;
    private AgendamentoStatus status;
}
