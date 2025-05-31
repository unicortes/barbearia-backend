package br.org.unicortes.barbearia.services.interfaces;

import br.org.unicortes.barbearia.dtos.HorarioLivreDTO;
import br.org.unicortes.barbearia.models.HorarioLivre;

import java.util.List;

public interface IHorarioLivreService {
    List<HorarioLivre> listarTodos();
    HorarioLivre buscarPorId(Long id);
    HorarioLivre criar(HorarioLivre horarioLivre);
    HorarioLivre atualizar(Long id, HorarioLivre dto);
    void remover(Long id);
}
