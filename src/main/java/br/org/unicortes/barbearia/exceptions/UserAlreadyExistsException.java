package br.org.unicortes.barbearia.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {super("Usuário já existe");}
}
