package br.org.unicortes.barbearia.service;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import br.org.unicortes.barbearia.model.Promocao;

public interface PromocaoServiceInterface {

    public Promocao savePromocao(Promocao promocao);
    public Promocao updatePromocao(Long id, Promocao promocao);
    public void deletePromocao(Long id);
}
