package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.Produto;
import br.org.unicortes.barbearia.repositories.ProdutoRepository;
import br.org.unicortes.barbearia.services.interfaces.IProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService implements IProdutoService {

    private final ProdutoRepository produtoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto não encontrado"));
    }

    @Override
    @Transactional
    public Produto criar(Produto novoProduto) {
        return produtoRepository.save(novoProduto);
    }

    @Override
    @Transactional
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto existente = buscarPorId(id);
        atualizarDadosProduto(existente, produtoAtualizado);
        return produtoRepository.save(existente);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void ativarProduto(Long id) {
        Produto produto = buscarPorId(id);
        produto.setAtivo(true);
        produtoRepository.save(produto);
    }

    @Override
    @Transactional
    public void desativarProduto(Long id) {
        Produto produto = buscarPorId(id);
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    private void atualizarDadosProduto(Produto existente, Produto atualizado) {
        if (atualizado.getNome() != null) {
            existente.setNome(atualizado.getNome());
        }
        if (atualizado.getDescricao() != null) {
            existente.setDescricao(atualizado.getDescricao());
        }
        if (atualizado.getCategoria() != null) {
            existente.setCategoria(atualizado.getCategoria());
        }
        if (atualizado.getPrecoCusto() != null) {
            existente.setPrecoCusto(atualizado.getPrecoCusto());
        }
        if (atualizado.getPrecoVenda() != null) {
            existente.setPrecoVenda(atualizado.getPrecoVenda());
        }
        if (atualizado.getQuantidadeEstoque() != null) {
            existente.setQuantidadeEstoque(atualizado.getQuantidadeEstoque());
        }
        if (atualizado.getDataValidade() != null) {
            existente.setDataValidade(atualizado.getDataValidade());
        }
        if (atualizado.isAtivo()) {
            existente.setAtivo(true);
        }
    }
}
