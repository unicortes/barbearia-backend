package br.org.unicortes.barbearia.models;

import java.time.LocalDate;

import br.org.unicortes.barbearia.dtos.PromocaoDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_promocoes")
public class Promocao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "O código da promoção é obrigatório")
    private String codigoPromo;

    @NotBlank(message = "A categoria é obrigatória")
    private String categoria;

    @NotNull(message = "O desconto é obrigatório")
    private double desconto;

    @NotNull(message = "A disponibilidade é obrigatória")
    private boolean disponibilidade;
    
    @NotNull(message = "A data de início é obrigatória")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate inicio;

    @NotNull(message = "A data final é obrigatória")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fim;
}
