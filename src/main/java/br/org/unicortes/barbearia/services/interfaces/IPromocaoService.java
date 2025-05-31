package br.org.unicortes.barbearia.services.interfaces;

import br.org.unicortes.barbearia.models.Promocao;

import java.util.List;

public interface IPromocaoService {
    List<Promocao> listarTodos();
    Promocao buscarPorId(Long id);
    Promocao criar(Promocao novaPromocao);
    Promocao atualizar(Long id, Promocao promocaoAtualizada);
    void remover(Long id);
}
