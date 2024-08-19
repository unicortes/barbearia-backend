package br.org.unicortes.barbearia.services;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import br.org.unicortes.barbearia.models.Promocao;

public interface PromocaoServiceInterface {

    public Promocao savePromocao(Promocao promocao);
    public Promocao updatePromocao(Long id, Promocao promocao);
    public void deletePromocao(Long id);
}
