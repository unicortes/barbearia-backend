package br.org.unicortes.barbearia.services.interfaces;

import br.org.unicortes.barbearia.models.CartaoFidelidade;

import java.util.List;

public interface ICartaoFidelidadeService {
    List<CartaoFidelidade> listarTodos();
    CartaoFidelidade buscarPorId(Long id);
    CartaoFidelidade criar(CartaoFidelidade novoCartao);
    CartaoFidelidade atualizarPontos(Long id, int novosPontos);
    void remover(Long id);
}
