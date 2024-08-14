package br.org.unicortes.barbearia.model;

import java.time.LocalDate;
import java.util.HashSet;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
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

    private String titulo;
    private String descricao;
    private String codigoPromocao;
    private String categoria;
    private double desconto;
    private boolean disponibilidade;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFim;

    @ManyToMany
    @JoinTable(
        name = "promocao_produto",
        joinColumns = @JoinColumn(name = "promocao_id"),
        inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private Set<Produto> produtos = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "promocao_servico",
        joinColumns = @JoinColumn(name = "promocao_id"),
        inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private Set<Servico> servicos = new HashSet<>();


   

    

    

}
