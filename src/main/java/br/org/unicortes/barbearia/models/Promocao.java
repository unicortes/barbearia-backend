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
public class Promocao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "O código da promoção é obrigatório")
    private String codigoPromocao;

    @NotBlank(message = "A categoria é obrigatória")
    private String categoria;

    @NotNull(message = "O desconto é obrigatório")
    private double desconto;

    @NotNull(message = "A disponibilidade é obrigatório")
    private boolean disponibilidade;
    
    @NotNull(message = "A data de início é obrigatória")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;

    @NotNull(message = "A data final é obrigatória")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFim;
}