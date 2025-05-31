package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.ConflitoException;
import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.Estoque;
import br.org.unicortes.barbearia.repositories.EstoqueRepository;
import br.org.unicortes.barbearia.services.interfaces.IEstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueService implements IEstoqueService {

    private final EstoqueRepository estoqueRepository;

    @Transactional(readOnly = true)
    public List<Estoque> listarTodos() {
        return estoqueRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Estoque buscarPorId(Long id) {
        return estoqueRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Estoque não encontrado"));
    }

    @Transactional
    public Estoque criar(Estoque novoEstoque) {
        validarConflitos(novoEstoque, null);
        return estoqueRepository.save(novoEstoque);
    }

    @Transactional
    public Estoque atualizar(Long id, Estoque estoqueAtualizado) {
        Estoque estoqueExistente = buscarPorId(id);
        validarConflitos(estoqueAtualizado, id);
        atualizarDadosEstoque(estoqueExistente, estoqueAtualizado);
        return estoqueRepository.save(estoqueExistente);
    }

    @Transactional
    public void remover(Long id) {
        validarExistencia(id);
        estoqueRepository.deleteById(id);
    }

    private void validarExistencia(Long id) {
        if (!estoqueRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Estoque não encontrado");
        }
    }

    private void validarConflitos(Estoque estoque, Long idIgnorado) {
        Long idParaVerificar = extrairIdOuValorNegativo(idIgnorado);
        boolean existeConflito = estoqueRepository.existsByProdutoAndStatusAndIdNot(
                estoque.getProduto(),
                estoque.getStatus(),
                idParaVerificar);
        if (existeConflito) {
            throw new ConflitoException("Já existe um estoque com o mesmo produto e status");
        }
    }

    private Long extrairIdOuValorNegativo(Long id) {
        if (id == null) {
            return -1L;
        }
        return id;
    }

    private void atualizarDadosEstoque(Estoque existente, Estoque atualizado) {
        if (atualizado.getProduto() != null) {
            existente.setProduto(atualizado.getProduto());
        }
        if (atualizado.getQuantidade() != null) {
            existente.setQuantidade(atualizado.getQuantidade());
        }
        if (atualizado.getStatus() != null) {
            existente.setStatus(atualizado.getStatus());
        }
        if (atualizado.getPrecoCusto() != null) {
            existente.setPrecoCusto(atualizado.getPrecoCusto());
        }
        if (atualizado.getLimiteEstoqueBaixo() != null) {
            existente.setLimiteEstoqueBaixo(atualizado.getLimiteEstoqueBaixo());
        }
    }
}
