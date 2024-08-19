package br.org.unicortes.barbearia.exceptions;

public class BarberAlreadyExistsException extends RuntimeException{
    public BarberAlreadyExistsException(String name) {
        super("O Barbeiro '" + name + "' já existe.");
        }
}
