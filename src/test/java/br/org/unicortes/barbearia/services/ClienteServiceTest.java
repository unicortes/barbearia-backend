package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.ConflitoException;
import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.Cliente;
import br.org.unicortes.barbearia.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setName("João");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("11999999999");
        cliente.setNascimento(LocalDate.of(1990, 1, 1));
        cliente.setIsAtivo(true);
    }

    @Test
    void listarTodos() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        List<Cliente> result = clienteService.listarTodos();
        assertEquals(1, result.size());
        verify(clienteRepository).findAll();
    }

    @Test
    void buscarClientePorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        Cliente result = clienteService.buscarPorId(1L);
        assertEquals(cliente, result);
    }

    @Test
    void buscarClienteInexistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntidadeNaoEncontradaException.class, () -> clienteService.buscarPorId(1L));
    }

    @Test
    void criarClienteSucesso() {
        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(clienteRepository.existsByTelefone(cliente.getTelefone())).thenReturn(false);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.criar(cliente);

        assertEquals(cliente, result);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void criarClienteEmailDuplicado() {
        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(true);
        assertThrows(ConflitoException.class, () -> clienteService.criar(cliente));
    }

    @Test
    void criarClienteTelDuplicado() {
        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(clienteRepository.existsByTelefone(cliente.getTelefone())).thenReturn(true);
        assertThrows(ConflitoException.class, () -> clienteService.criar(cliente));
    }

    @Test
    void atualizarClienteSucesso() {
        Cliente atualizado = new Cliente();
        atualizado.setName("João Atualizado");
        atualizado.setEmail("novo@email.com");
        atualizado.setTelefone("11888888888");
        atualizado.setNascimento(LocalDate.of(1995, 2, 2));
        atualizado.setIsAtivo(false);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByEmailAndIdNot("novo@email.com", 1L)).thenReturn(false);
        when(clienteRepository.existsByTelefoneAndIdNot("11888888888", 1L)).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente result = clienteService.atualizar(1L, atualizado);

        assertEquals("João Atualizado", result.getName());
        assertEquals("novo@email.com", result.getEmail());
        assertEquals("11888888888", result.getTelefone());
        assertFalse(result.getIsAtivo());
    }

    @Test
    void removerClienteSucesso() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        clienteService.remover(1L);
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void removerClienteInexistente() {
        when(clienteRepository.existsById(1L)).thenReturn(false);
        assertThrows(EntidadeNaoEncontradaException.class, () -> clienteService.remover(1L));
    }

    @Test
    void ativarClienteSucesso() {
        cliente.setIsAtivo(false);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        clienteService.ativarCliente(1L);
        assertTrue(cliente.getIsAtivo());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void desativarClienteSucesso() {
        cliente.setIsAtivo(true);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        clienteService.desativarCliente(1L);
        assertFalse(cliente.getIsAtivo());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void ativarClienteExistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntidadeNaoEncontradaException.class, () -> clienteService.ativarCliente(1L));
    }

    @Test
    void desativarClienteInexistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntidadeNaoEncontradaException.class, () -> clienteService.desativarCliente(1L));
    }
}
