package br.org.unicortes.barbearia.exceptions;

public class ClienteAlreadyExistsException extends RuntimeException {
    public ClienteAlreadyExistsException(String name) {
        super("O Cliente '" + name + "' já existe.");
    }
}
