package br.org.unicortes.barbearia.enums;

public enum ServiceAppointmentStatus {
    PENDENTE("Confirmação de agendamento pendente"),
    CONFIRMADO("Agendamento confirmado"),
    CANCELADO("Agendamento cancelado");

    private String status;

    private ServiceAppointmentStatus(String status) {
        this.status = status;
    }
}
