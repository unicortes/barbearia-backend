package br.org.unicortes.barbearia.dtos;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioDTO {
     
    private Long id;
    private String name;
    private String password;
    private String email;
    private String role;
    private String token;
}
