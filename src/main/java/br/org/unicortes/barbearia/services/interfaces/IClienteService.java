package br.org.unicortes.barbearia.services.interfaces;

import br.org.unicortes.barbearia.models.Cliente;

import java.util.List;

public interface IClienteService {

    List<Cliente> listarTodos();
    Cliente buscarPorId(Long id);
    Cliente criar(Cliente cliente);
    Cliente atualizar(Long id, Cliente clienteAtualizado);
    void remover(Long id);
    void ativarCliente(Long id);
    void desativarCliente(Long id);
}
