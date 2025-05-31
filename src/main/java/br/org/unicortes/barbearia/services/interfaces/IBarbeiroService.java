package br.org.unicortes.barbearia.services.interfaces;

import br.org.unicortes.barbearia.models.Barbeiro;
import java.util.List;

public interface IBarbeiroService {

    List<Barbeiro> listarTodos();

    Barbeiro buscarPorId(Long id);

    Barbeiro criar(Barbeiro novoBarbeiro);

    Barbeiro atualizar(Long id, Barbeiro barbeiroAtualizado);

    void remover(Long id);

    void ativarBarbeiro(Long id);

    void desativarBarbeiro(Long id);
}
