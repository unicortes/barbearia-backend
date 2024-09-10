package br.org.unicortes.barbearia.models;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import br.org.unicortes.barbearia.models.Product;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "tb_promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "A descrição é obrigatória")
    private String description;

    @NotBlank(message = "O código da promoção é obrigatório")
    private String promotionCode;

    @NotBlank(message = "A categoria é obrigatória")
    private String category;

    @NotNull(message = "O desconto é obrigatório")
    private double discount;

    @NotNull(message = "A disponibilidade é obrigatória")
    private boolean availability;
    
    @NotNull(message = "A data de início é obrigatória")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @NotNull(message = "A data final é obrigatória")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    public boolean getAvailability() {
        return this.availability;
    }
}