package br.org.unicortes.barbearia.services.interfaces;

import br.org.unicortes.barbearia.models.Servico;

import java.util.List;

public interface IServicoService {
    List<Servico> listarTodos();
    Servico buscarPorId(Long id);
    Servico criar(Servico novoServico);
    Servico atualizar(Long id, Servico servicoAtualizado);
    void remover(Long id);
}
