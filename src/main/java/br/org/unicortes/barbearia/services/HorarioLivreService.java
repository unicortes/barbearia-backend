package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.HorarioLivre;
import br.org.unicortes.barbearia.repositories.HorarioLivreRepository;
import br.org.unicortes.barbearia.services.interfaces.IHorarioLivreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioLivreService implements IHorarioLivreService {

    private final HorarioLivreRepository horarioLivreRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HorarioLivre> listarTodos() {
        return horarioLivreRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public HorarioLivre buscarPorId(Long id) {
        return horarioLivreRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Horário livre não encontrado com ID: " + id));
    }

    @Override
    @Transactional
    public HorarioLivre criar(HorarioLivre novoHorarioLivre) {
        validarConflitos(novoHorarioLivre, null);
        return horarioLivreRepository.save(novoHorarioLivre);
    }

    @Override
    @Transactional
    public HorarioLivre atualizar(Long id, HorarioLivre horarioLivreAtualizado) {
        HorarioLivre existente = buscarPorId(id);
        validarConflitos(horarioLivreAtualizado, id);

        existente.setBarbeiro(horarioLivreAtualizado.getBarbeiro());
        existente.setServico(horarioLivreAtualizado.getServico());
        existente.setInicio(horarioLivreAtualizado.getInicio());
        existente.setFim(horarioLivreAtualizado.getFim());
        existente.setDisponivel(horarioLivreAtualizado.isDisponivel());

        return horarioLivreRepository.save(existente);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        HorarioLivre existente = buscarPorId(id);
        horarioLivreRepository.delete(existente);
    }

    private void validarConflitos(HorarioLivre horarioLivre, Long idExistente) {
        List<HorarioLivre> conflitos = horarioLivreRepository.findConflictingSchedules(
                horarioLivre.getBarbeiro().getId(),
                horarioLivre.getInicio(),
                horarioLivre.getFim(),
                idExistente != null ? idExistente : -1L
        );

        if (!conflitos.isEmpty()) {
            throw new RuntimeException("Conflito: Já existe um horário livre que conflita com o intervalo informado para este barbeiro.");
        }
    }
}
