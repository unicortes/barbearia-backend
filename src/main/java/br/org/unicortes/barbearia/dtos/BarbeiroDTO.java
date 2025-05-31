package br.org.unicortes.barbearia.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class BarbeiroDTO {
    private Long id;
    private String name;
    private String email;
    private String telefone;
    private String cpf;
    private BigDecimal salario;
    private String endereco;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private boolean ativo;
    private String especialidades;
    private Long usuarioId;

    private List<HorarioLivreDTO> horariosDisponiveis;
    private List<AgendamentoDTO> agendamentos;
}
