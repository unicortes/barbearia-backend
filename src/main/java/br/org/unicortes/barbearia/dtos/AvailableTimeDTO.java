package br.org.unicortes.barbearia.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableTimeDTO {
    private Long id;
    private Long barberId;
    private Long serviceId;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private boolean isScheduled;
}
