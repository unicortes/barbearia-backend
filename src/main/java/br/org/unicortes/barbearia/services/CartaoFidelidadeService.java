package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.CartaoFidelidade;
import br.org.unicortes.barbearia.models.Cliente;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.CartaoFidelidadeRepository;
import br.org.unicortes.barbearia.repositories.ClienteRepository;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import br.org.unicortes.barbearia.services.interfaces.ICartaoFidelidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoFidelidadeService implements ICartaoFidelidadeService {

    private final CartaoFidelidadeRepository cartaoFidelidadeRepository;
    private final ClienteRepository clienteRepository;
    private final ServicoRepository servicoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CartaoFidelidade> listarTodos() {
        return cartaoFidelidadeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CartaoFidelidade buscarPorId(Long id) {
        return cartaoFidelidadeRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cartão de fidelidade não encontrado"));
    }

    @Override
    @Transactional
    public CartaoFidelidade criar(CartaoFidelidade novoCartao) {
        Cliente cliente = clienteRepository.findById(novoCartao.getCliente().getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado"));

        Servico servico = servicoRepository.findById(novoCartao.getServico().getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Serviço não encontrado"));

        novoCartao.setCliente(cliente);
        novoCartao.setServico(servico);

        return cartaoFidelidadeRepository.save(novoCartao);
    }

    @Override
    @Transactional
    public CartaoFidelidade atualizarPontos(Long id, int novosPontos) {
        CartaoFidelidade cartaoExistente = cartaoFidelidadeRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cartão de fidelidade não encontrado"));

        if (novosPontos >= cartaoExistente.getPontos()) {
            cartaoExistente.setPontos(novosPontos);
        }

        return cartaoFidelidadeRepository.save(cartaoExistente);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        if (!cartaoFidelidadeRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Cartão de fidelidade não encontrado");
        }
        cartaoFidelidadeRepository.deleteById(id);
    }
}
