package br.org.unicortes.barbearia.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O campo 'name' é obrigatório")
    private String name;

    @NotBlank(message = "O 'senha' é obrigatório")
    private String password;

    @NotBlank(message = "O campo 'email' é obrigatório")
    private String email;

    @NotBlank(message = "O campo 'role' é obrigatório")
    private String role;
}
