package br.org.unicortes.barbearia.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PromotionDTO {
    private Long id;
    private String name;
    private String description;
    private String promotionCode;
    private String category;
    private double discount;
    private boolean availability;
    private LocalDate startDate;
    private LocalDate endDate;

    public boolean getAvailability() {
        return this.availability;
    }
}