package br.org.unicortes.barbearia.dtos;

import br.org.unicortes.barbearia.enums.Roles;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioDTO {
    private Long id;
    private String name;
    private String email;
    private Roles role;
    private boolean ativo;
    private LocalDateTime tokenExpiration;
}
