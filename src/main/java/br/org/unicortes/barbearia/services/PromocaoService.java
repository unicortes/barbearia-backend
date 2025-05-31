package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.Promocao;
import br.org.unicortes.barbearia.repositories.PromocaoRepository;
import br.org.unicortes.barbearia.services.interfaces.IPromocaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromocaoService implements IPromocaoService {

    private final PromocaoRepository promocaoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Promocao> listarTodos() {
        return promocaoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Promocao buscarPorId(Long id) {
        return promocaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Promoção não encontrada"));
    }

    @Override
    @Transactional
    public Promocao criar(Promocao novaPromocao) {
        return promocaoRepository.save(novaPromocao);
    }

    @Override
    @Transactional
    public Promocao atualizar(Long id, Promocao promocaoAtualizada) {
        Promocao existente = buscarPorId(id);
        atualizarDadosPromocao(existente, promocaoAtualizada);
        return promocaoRepository.save(existente);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        if (!promocaoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Promoção não encontrada");
        }
        promocaoRepository.deleteById(id);
    }

    private void atualizarDadosPromocao(Promocao existente, Promocao atualizada) {
        if (atualizada.getNome() != null) {
            existente.setNome(atualizada.getNome());
        }
        if (atualizada.getDescricao() != null) {
            existente.setDescricao(atualizada.getDescricao());
        }
        if (atualizada.getCodigoPromo() != null) {
            existente.setCodigoPromo(atualizada.getCodigoPromo());
        }
        if (atualizada.getCategoria() != null) {
            existente.setCategoria(atualizada.getCategoria());
        }
        if (atualizada.getDesconto() != existente.getDesconto()) {
            existente.setDesconto(atualizada.getDesconto());
        }
        if (atualizada.isDisponibilidade() != existente.isDisponibilidade()) {
            existente.setDisponibilidade(atualizada.isDisponibilidade());
        }
        if (atualizada.getInicio() != null) {
            existente.setInicio(atualizada.getInicio());
        }
        if (atualizada.getFim() != null) {
            existente.setFim(atualizada.getFim());
        }
    }
}
