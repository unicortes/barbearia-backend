package br.org.unicortes.barbearia.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import br.org.unicortes.barbearia.models.Product;
import jakarta.validation.constraints.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor 
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

    @NotBlank(message = "O desconto é obrigatório")
    private double desconto;

    @NotBlank(message = "A disponibilidade é obrigatório")
    private boolean disponibilidade;
    
    @NotNull(message = "A data de início é obrigatória")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;

    @NotNull(message = "A data final é obrigatória")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFim;

    @ManyToMany
    @JoinTable(
        name = "promocao_product",
        joinColumns = @JoinColumn(name = "promocao_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> produtos = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "promocao_servico",
        joinColumns = @JoinColumn(name = "promocao_id"),
        inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private Set<Servico> servicos = new HashSet<>();


   

    

    

}
