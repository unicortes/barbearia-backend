package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import br.org.unicortes.barbearia.services.interfaces.IServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService implements IServicoService {

    private final ServicoRepository servicoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Serviço não encontrado"));
    }

    @Override
    @Transactional
    public Servico criar(Servico novoServico) {
        return servicoRepository.save(novoServico);
    }

    @Override
    @Transactional
    public Servico atualizar(Long id, Servico servicoAtualizado) {
        Servico existente = buscarPorId(id);
        atualizarDadosServico(existente, servicoAtualizado);
        return servicoRepository.save(existente);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Serviço não encontrado");
        }
        servicoRepository.deleteById(id);
    }

    private void atualizarDadosServico(Servico existente, Servico atualizado) {
        if (atualizado.getNome() != null) {
            existente.setNome(atualizado.getNome());
        }
        if (atualizado.getDescricao() != null) {
            existente.setDescricao(atualizado.getDescricao());
        }
        if (atualizado.getPreco() != null) {
            existente.setPreco(atualizado.getPreco());
        }
        if (atualizado.getDuracaoPadraoMinutos() != existente.getDuracaoPadraoMinutos()) {
            existente.setDuracaoPadraoMinutos(atualizado.getDuracaoPadraoMinutos());
        }
        if (atualizado.isAtivo()) {
            existente.setAtivo(true);
        }
    }
}
