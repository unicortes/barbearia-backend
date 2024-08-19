package br.org.unicortes.barbearia.models;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long barbeiroId;

    @NotBlank(message = "O nome é obrigatório")
    private String barbeiroNome;

    @Email(message = "O e-mail deve ser válido")
    @NotBlank(message = "O e-mail é obrigatório")
    private String barbeiroEmail;

    @NotBlank(message = "O telefone é obrigatório")
    private String barbeiroTelefone;

    @NotBlank(message = "O CPF é obrigatório")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos")
    private String barbeiroCpf;

    @NotNull(message = "O salário é obrigatório")
    @PositiveOrZero(message = "O salário deve ser um valor positivo ou zero")
    private Double barbeiroSalario;

    @NotBlank(message = "O endereço é obrigatório")
    private String barbeiroEndereco;

    @NotNull(message = "A data de admissão é obrigatória")
    private LocalDate barbeiroDataDeAdimissao;

    @NotBlank(message = "Os horários de atendimento são obrigatórios")
    private String barbeiroHorariosAtendimento;

    // Construtor padrão
    public Barbeiro() {
    }

    // Construtor com todos os campos
    public Barbeiro(String barbeiroNome, String barbeiroEmail, String barbeiroTelefone, String barbeiroCpf, Double barbeiroSalario, String barbeiroEndereco, LocalDate barbeiroDataDeAdimissao, String barbeiroHorariosAtendimento) {
        this.barbeiroNome = barbeiroNome;
        this.barbeiroEmail = barbeiroEmail;
        this.barbeiroTelefone = barbeiroTelefone;
        this.barbeiroCpf = barbeiroCpf;
        this.barbeiroSalario = barbeiroSalario;
        this.barbeiroEndereco = barbeiroEndereco;
        this.barbeiroDataDeAdimissao = barbeiroDataDeAdimissao;
        this.barbeiroHorariosAtendimento = barbeiroHorariosAtendimento;
    }

    // Getters e Setters
    public Long getBarbeiroById() {
        return barbeiroId;
    }

    public void setBarbeiroById(Long barbeiroId) {
        this.barbeiroId = barbeiroId;
    }

    public String getBarbeiroByNome() {
        return barbeiroNome;
    }

    public void setBarbeiroByNome(String barbeiroNome) {
        this.barbeiroNome = barbeiroNome;
    }

    public String getBarbeiroByEmail() {
        return barbeiroEmail;
    }

    public void setBarbeiroByEmail(String barbeiroEmail) {
        this.barbeiroEmail = barbeiroEmail;
    }

    public String getBarbeiroByTelefone() {
        return barbeiroTelefone;
    }

    public void setBarbeiroByTelefone(String barbeiroTelefone) {
        this.barbeiroTelefone = barbeiroTelefone;
    }

    public String getBarbeiroByCpf() {
        return barbeiroCpf;
    }

    public void setBarbeiroByCpf(String barbeiroCpf) {
        this.barbeiroCpf = barbeiroCpf;
    }

    public Double getBarbeiroBySalario() {
        return barbeiroSalario;
    }

    public void setBarbeiroBySalario(Double barbeiroSalario) {
        this.barbeiroSalario = barbeiroSalario;
    }

    public String getBarbeiroByEndereco() {
        return barbeiroEndereco;
    }

    public void setBarbeiroByEndereco(String barbeiroEndereco) {
        this.barbeiroEndereco = barbeiroEndereco;
    }

    public LocalDate getBarbeiroByDataAdmissao() {
        return barbeiroDataDeAdimissao;
    }

    public void setBarbeiroByDataAdmissao(LocalDate barbeiroDataDeAdimissao) {
        this.barbeiroDataDeAdimissao = barbeiroDataDeAdimissao;
    }

    public String getBarbeiroByHorariosAtendimento() {
        return barbeiroHorariosAtendimento;
    }

    public void setBarbeiroByHorariosAtendimento(String barbeiroHorariosAtendimento) {
        this.barbeiroHorariosAtendimento = barbeiroHorariosAtendimento;
    }
}
