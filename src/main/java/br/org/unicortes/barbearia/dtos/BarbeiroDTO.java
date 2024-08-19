package br.org.unicortes.barbearia.dtos;

import java.time.LocalDate;
import lombok.Data;


@Data
public class BarbeiroDTO {
    private Long barbeiroId;
    private String barbeiroNome;
    private String barbeiroEmail;
    private String barbeiroTelefone;
    private String barbeiroCpf;
    private Double barbeiroSalario;
    private String barbeiroEndereco;
    private LocalDate barbeiroDataDeAdimissao;
    private String barbeiroHorariosAtendimento;
}
