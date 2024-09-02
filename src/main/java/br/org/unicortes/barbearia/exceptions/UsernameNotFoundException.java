package br.org.unicortes.barbearia.exceptions;

public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException(String name){
        super("Usuário não encontrado");
    }
}
