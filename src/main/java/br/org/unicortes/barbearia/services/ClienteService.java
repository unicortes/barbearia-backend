package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.ConflitoException;
import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.Cliente;
import br.org.unicortes.barbearia.repositories.ClienteRepository;
import br.org.unicortes.barbearia.services.interfaces.IClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado"));
    }

    @Override
    @Transactional
    public Cliente criar(Cliente novoCliente) {
        validarConflitos(novoCliente, null);
        return clienteRepository.save(novoCliente);
    }

    @Override
    @Transactional
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = buscarPorId(id);
        validarConflitos(clienteAtualizado, id);
        atualizarDadosCliente(clienteExistente, clienteAtualizado);
        return clienteRepository.save(clienteExistente);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Cliente não encontrado");
        }
        clienteRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void ativarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado"));
        cliente.setIsAtivo(true);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void desativarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado"));
        cliente.setIsAtivo(false);
        clienteRepository.save(cliente);
    }

    private void validarConflitos(Cliente cliente, Long idIgnorado) {
        if (this.emailEmUso(cliente.getEmail(), idIgnorado)) {
            throw new ConflitoException("Email já está em uso por outro cliente");
        }

        if (this.telefoneEmUso(cliente.getTelefone(), idIgnorado)) {
            throw new ConflitoException("Telefone já está em uso por outro cliente");
        }
    }

    private boolean telefoneEmUso(String telefone, Long idIgnorado) {
        if (idIgnorado == null) {
            return clienteRepository.existsByTelefone(telefone);
        }
        return clienteRepository.existsByTelefoneAndIdNot(telefone, idIgnorado);
    }

    private boolean emailEmUso(String email, Long idIgnorado) {
        if (idIgnorado == null) {
            return clienteRepository.existsByEmail(email);
        }
        return clienteRepository.existsByEmailAndIdNot(email, idIgnorado);
    }

    private void atualizarDadosCliente(Cliente existente, Cliente atualizado) {
        if (atualizado.getName() != null) {
            existente.setName(atualizado.getName());
        }
        if (atualizado.getEmail() != null) {
            existente.setEmail(atualizado.getEmail());
        }
        if (atualizado.getTelefone() != null) {
            existente.setTelefone(atualizado.getTelefone());
        }
        if (atualizado.getNascimento() != null) {
            existente.setNascimento(atualizado.getNascimento());
        }
        if (atualizado.getIsAtivo() != null) {
            existente.setIsAtivo(atualizado.getIsAtivo());
        }
    }
}
