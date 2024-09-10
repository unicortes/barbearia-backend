package br.org.unicortes.barbearia.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableTimeDTO {
    private Long id;
    private Long barber;
    private Long service;
    private Date timeStart;
    private Date timeEnd;
    private boolean isScheduled;
}
