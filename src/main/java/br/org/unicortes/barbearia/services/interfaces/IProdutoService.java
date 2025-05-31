package br.org.unicortes.barbearia.services.interfaces;

import br.org.unicortes.barbearia.models.Produto;

import java.util.List;

public interface IProdutoService {
    List<Produto> listarTodos();
    Produto buscarPorId(Long id);
    Produto criar(Produto novoProduto);
    Produto atualizar(Long id, Produto produtoAtualizado);
    void remover(Long id);
    void ativarProduto(Long id);
    void desativarProduto(Long id);
}
