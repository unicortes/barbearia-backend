package br.org.unicortes.barbearia.exceptions;

public class ClientAlreadyExistsException extends RuntimeException {
    public ClientAlreadyExistsException(String name) {
        super("O Cliente '" + name + "' já existe.");
    }
}
