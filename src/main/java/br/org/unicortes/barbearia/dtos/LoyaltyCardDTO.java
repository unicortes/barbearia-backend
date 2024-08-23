package br.org.unicortes.barbearia.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyCardDTO {

    private Long id;
    private Long clientId;
    private Date dateAdmission;
    private Long serviceId;
    private int points;
}

