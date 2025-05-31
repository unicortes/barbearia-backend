package br.org.unicortes.barbearia.enums;

public enum EstoqueStatus {
    EM_USO("Em Uso"),
    LACRADO("Lacrado");

    private String status;

    private EstoqueStatus(String status) {
        this.status = status;
    }
}