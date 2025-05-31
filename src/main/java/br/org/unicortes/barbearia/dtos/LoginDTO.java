package br.org.unicortes.barbearia.dtos;

import br.org.unicortes.barbearia.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    private Usuario usuario;
    private String token;

    public LoginDTO(Optional<Usuario> user, String token) {
    }
}
