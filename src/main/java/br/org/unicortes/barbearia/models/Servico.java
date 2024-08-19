package br.org.unicortes.barbearia.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity(name="tb_servico")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long servicoId;

    @NotBlank(message = "O campo 'nome' é obrigatório")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
    private String name;

    @NotBlank(message = "O campo 'descrição' é obrigatório")
    @Size(max = 255, message = "A descrição deve ter menos que 255 caracteres")
    private String description;

    @NotNull(message = "O campo 'preço' é obrigatório")
    @DecimalMin(value ="0.0", message = "O campo 'preço' deve ser maior ou igual a 0")
    private double price;
}
