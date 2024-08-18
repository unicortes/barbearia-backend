package br.org.unicortes.barbearia.enums;

public enum StockStatus {
    EM_USO("Em Uso"),
    LACRADO("Lacrado");

    private String status;

    private StockStatus(String status) {
        this.status = status;
    }
}