package br.org.unicortes.barbearia.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteDTO {
    private Long id;
    private String name;
    private String telefone;
    private LocalDate nascimento;
    private String email;
    private LocalDate criadoEm;
    private Boolean isAtivo;
    private Long usuarioId;
}
