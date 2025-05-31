package br.org.unicortes.barbearia.requests;

import jakarta.validation.constraints.NotBlank;

public record NovaSenhaRequest(@NotBlank(message = "A nova senha não pode estar em branco") String senha) {

}
