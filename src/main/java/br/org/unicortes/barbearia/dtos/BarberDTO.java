package br.org.unicortes.barbearia.dtos;

import java.time.LocalDate;
import lombok.Data;

@Data
public class BarberDTO {
    private Long barberId;
    private String name;
    private String barberEmail;
    private String barberTelefone;
    private String barberCpf;
    private Double barberSalario;
    private String barberEndereco;
    private LocalDate barberDataDeAdimissao;
    private String barberHorariosAtendimento;
}
