package br.org.unicortes.barbearia.exceptions;

public class ServicoNotFoundException extends Exception{

    public ServicoNotFoundException(String message) {
        super(message);
    }

    public ServicoNotFoundException() {
        super("Servico não encontrado!");
    }
}
