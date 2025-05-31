package br.org.unicortes.barbearia.enums;

import lombok.Getter;

@Getter
public enum AgendamentoStatus {
    AGENDADO("Agendado", true, false),
    CONFIRMADO("Confirmado", true, false),
    EM_ANDAMENTO("Em andamento", false, false),
    CONCLUIDO("Concluído", false, true),
    CANCELADO("Cancelado", false, true),
    FALTOU("Cliente faltou", false, true);

    private final String descricao;
    private final boolean editavel;
    private final boolean finalizado;

    AgendamentoStatus(String descricao, boolean editavel, boolean finalizado) {
        this.descricao = descricao;
        this.editavel = editavel;
        this.finalizado = finalizado;
    }

    public boolean podeCancelar() {
        return this.editavel && !this.finalizado;
    }

    public boolean podeConfirmar() {
        return this == AGENDADO;
    }

    public boolean podeIniciar() {
        return this == CONFIRMADO;
    }

    public boolean podeConcluir() {
        return this == EM_ANDAMENTO;
    }

    public static boolean isStatusValido(AgendamentoStatus current, AgendamentoStatus newStatus) {
        return switch (current) {
            case AGENDADO -> newStatus == CONFIRMADO || newStatus == CANCELADO;
            case CONFIRMADO -> newStatus == EM_ANDAMENTO || newStatus == CANCELADO;
            case EM_ANDAMENTO -> newStatus == CONCLUIDO || newStatus == FALTOU;
            default -> false;
        };
    }
}