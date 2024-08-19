package br.org.unicortes.barbearia.exceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException (Long id) {
        super("Cliente com o id '" + id + "' não encontrado.");
    }
}