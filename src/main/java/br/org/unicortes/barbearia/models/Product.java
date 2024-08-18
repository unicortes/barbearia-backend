package br.org.unicortes.barbearia.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "tb_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O campo 'nome' é obrigatório")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
    private String name;

    @NotBlank(message = "O campo 'descrição' é obrigatório")
    @Size(max = 255, message = "A descrição deve ter menos que 255 caracteres")
    private String description;

    @NotBlank(message = "O campo 'categoria' é obrigatório")
    private String category;

    @NotNull(message = "O campo 'data de validade' é obrigatório")
    private Date expirationDate;

    @Min(value = 1, message = "O preço deve ser maior do que 0")
    private double cost;

    @NotBlank(message = "O campo 'tipo' é obrigatório")
    private String type;
}