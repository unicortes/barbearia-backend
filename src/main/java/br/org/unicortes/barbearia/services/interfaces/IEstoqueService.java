package br.org.unicortes.barbearia.services.interfaces;

import br.org.unicortes.barbearia.models.Estoque;

import java.util.List;

public interface IEstoqueService {
    List<Estoque> listarTodos();
    Estoque buscarPorId(Long id);
    Estoque criar(Estoque novoEstoque);
    Estoque atualizar(Long id, Estoque estoqueAtualizado);
    void remover(Long id);
}
