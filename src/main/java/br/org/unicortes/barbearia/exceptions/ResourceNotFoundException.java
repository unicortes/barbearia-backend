package br.org.unicortes.barbearia.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long id) {
        super("Barbeiro com o id '" + id + "' não encontrado.");
    }
}
