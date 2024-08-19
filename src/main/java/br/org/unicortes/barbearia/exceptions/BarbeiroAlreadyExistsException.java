package br.org.unicortes.barbearia.exceptions;

public class BarbeiroAlreadyExistsException extends RuntimeException{
    public BarbeiroAlreadyExistsException(String name) {
        super("O Barbeiro '" + name + "' já existe.");
        }
}
