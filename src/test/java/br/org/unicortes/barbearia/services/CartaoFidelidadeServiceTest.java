package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.CartaoFidelidade;
import br.org.unicortes.barbearia.models.Cliente;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.CartaoFidelidadeRepository;
import br.org.unicortes.barbearia.repositories.ClienteRepository;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartaoFidelidadeServiceTest {

    @Mock
    private CartaoFidelidadeRepository cartaoFidelidadeRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @InjectMocks
    private CartaoFidelidadeService service;

    private CartaoFidelidade cartao;
    private Cliente cliente;
    private Servico servico;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);

        servico = new Servico();
        servico.setId(1L);

        cartao = new CartaoFidelidade();
        cartao.setId(1L);
        cartao.setCliente(cliente);
        cartao.setServico(servico);
        cartao.setPontos(10);
    }

    @Test
    void listarTodos() {
        when(cartaoFidelidadeRepository.findAll()).thenReturn(List.of(cartao));
        List<CartaoFidelidade> result = service.listarTodos();
        assertEquals(1, result.size());
        assertEquals(cartao, result.get(0));
        verify(cartaoFidelidadeRepository).findAll();
    }

    @Test
    void buscarPorIdSucesso() {
        when(cartaoFidelidadeRepository.findById(1L)).thenReturn(Optional.of(cartao));
        CartaoFidelidade result = service.buscarPorId(1L);
        assertEquals(cartao, result);
        verify(cartaoFidelidadeRepository).findById(1L);
    }

    @Test
    void buscarPorIdErro() {
        when(cartaoFidelidadeRepository.findById(2L)).thenReturn(Optional.empty());
        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.buscarPorId(2L));
        assertTrue(ex.getMessage().contains("Cartão de fidelidade não encontrado"));
    }

    @Test
    void criarSucesso() {
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(servicoRepository.findById(servico.getId())).thenReturn(Optional.of(servico));
        when(cartaoFidelidadeRepository.save(cartao)).thenReturn(cartao);

        CartaoFidelidade criado = service.criar(cartao);

        assertEquals(cartao, criado);
        verify(clienteRepository).findById(cliente.getId());
        verify(servicoRepository).findById(servico.getId());
        verify(cartaoFidelidadeRepository).save(cartao);
    }

    @Test
    void criarErroCliente() {
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.criar(cartao));

        assertTrue(ex.getMessage().contains("Cliente não encontrado"));
        verify(clienteRepository).findById(cliente.getId());
        verify(servicoRepository, never()).findById(any());
        verify(cartaoFidelidadeRepository, never()).save(any());
    }

    @Test
    void criarErroServico() {
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(servicoRepository.findById(servico.getId())).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.criar(cartao));

        assertTrue(ex.getMessage().contains("Serviço não encontrado"));
        verify(clienteRepository).findById(cliente.getId());
        verify(servicoRepository).findById(servico.getId());
        verify(cartaoFidelidadeRepository, never()).save(any());
    }

    @Test
    void atualizarPontosSucesso() {
        when(cartaoFidelidadeRepository.findById(cartao.getId())).thenReturn(Optional.of(cartao));
        when(cartaoFidelidadeRepository.save(cartao)).thenReturn(cartao);

        CartaoFidelidade atualizado = service.atualizarPontos(cartao.getId(), 50);

        assertEquals(50, atualizado.getPontos());
        verify(cartaoFidelidadeRepository).findById(cartao.getId());
        verify(cartaoFidelidadeRepository).save(cartao);
    }

    @Test
    void atualizarPontosInexistente() {
        when(cartaoFidelidadeRepository.findById(99L)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.atualizarPontos(99L, 50));

        assertTrue(ex.getMessage().contains("Cartão de fidelidade não encontrado"));
        verify(cartaoFidelidadeRepository).findById(99L);
        verify(cartaoFidelidadeRepository, never()).save(any());
    }

    @Test
    void removerSucesso() {
        when(cartaoFidelidadeRepository.existsById(cartao.getId())).thenReturn(true);
        doNothing().when(cartaoFidelidadeRepository).deleteById(cartao.getId());

        assertDoesNotThrow(() -> service.remover(cartao.getId()));

        verify(cartaoFidelidadeRepository).existsById(cartao.getId());
        verify(cartaoFidelidadeRepository).deleteById(cartao.getId());
    }

    @Test
    void removerInexistente() {
        when(cartaoFidelidadeRepository.existsById(99L)).thenReturn(false);

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.remover(99L));

        assertTrue(ex.getMessage().contains("Cartão de fidelidade não encontrado"));
        verify(cartaoFidelidadeRepository).existsById(99L);
        verify(cartaoFidelidadeRepository, never()).deleteById(any());
    }
}
