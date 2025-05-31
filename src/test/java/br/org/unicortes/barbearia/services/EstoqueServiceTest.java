package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.enums.EstoqueStatus;
import br.org.unicortes.barbearia.exceptions.ConflitoException;
import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.Estoque;
import br.org.unicortes.barbearia.models.Produto;
import br.org.unicortes.barbearia.repositories.EstoqueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {

    private EstoqueRepository estoqueRepository;
    private EstoqueService estoqueService;

    @BeforeEach
    void setUp() {
        estoqueRepository = mock(EstoqueRepository.class);
        estoqueService = new EstoqueService(estoqueRepository);
    }

    @Test
    void listarTodos_deveRetornarLista() {
        List<Estoque> mockList = List.of(new Estoque(), new Estoque());
        when(estoqueRepository.findAll()).thenReturn(mockList);

        List<Estoque> resultado = estoqueService.listarTodos();

        assertEquals(2, resultado.size());
        verify(estoqueRepository).findAll();
    }

    @Test
    void buscarPorIdSucesso() {
        Estoque estoque = new Estoque();
        when(estoqueRepository.findById(1L)).thenReturn(Optional.of(estoque));

        Estoque resultado = estoqueService.buscarPorId(1L);

        assertNotNull(resultado);
        verify(estoqueRepository).findById(1L);
    }

    @Test
    void buscarPorIdErro() {
        when(estoqueRepository.findById(1L)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class,
                () -> estoqueService.buscarPorId(1L));
        assertTrue(ex.getMessage().contains("Estoque não encontrado"));
    }

    @Test
    void criarSucesso() {
        Estoque novo = criarEstoque();
        when(estoqueRepository.existsByProdutoAndStatusAndIdNot(any(), any(), anyLong())).thenReturn(false);
        when(estoqueRepository.save(novo)).thenReturn(novo);

        Estoque criado = estoqueService.criar(novo);

        assertEquals(novo, criado);
        verify(estoqueRepository).save(novo);
    }

    @Test
    void criarErro() {
        Estoque novo = criarEstoque();
        when(estoqueRepository.existsByProdutoAndStatusAndIdNot(any(), any(), anyLong())).thenReturn(true);

        assertThrows(ConflitoException.class, () -> estoqueService.criar(novo));
        verify(estoqueRepository, never()).save(any());
    }

    @Test
    void atualizarSucesso() {
        Estoque existente = criarEstoque();
        Estoque atualizado = criarEstoque();
        atualizado.setQuantidade(99);

        when(estoqueRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estoqueRepository.existsByProdutoAndStatusAndIdNot(any(), any(), anyLong())).thenReturn(false);
        when(estoqueRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Estoque resultado = estoqueService.atualizar(1L, atualizado);

        assertEquals(99, resultado.getQuantidade());
        verify(estoqueRepository).save(existente);
    }

    @Test
    void atualizarErro() {
        Estoque existente = criarEstoque();
        Estoque atualizado = criarEstoque();
        when(estoqueRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estoqueRepository.existsByProdutoAndStatusAndIdNot(any(), any(), anyLong())).thenReturn(true);

        assertThrows(ConflitoException.class, () -> estoqueService.atualizar(1L, atualizado));
        verify(estoqueRepository, never()).save(any());
    }

    @Test
    void removerSucesso() {
        when(estoqueRepository.existsById(1L)).thenReturn(true);

        estoqueService.remover(1L);

        verify(estoqueRepository).deleteById(1L);
    }

    @Test
    void removerErro() {
        when(estoqueRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntidadeNaoEncontradaException.class, () -> estoqueService.remover(1L));
        verify(estoqueRepository, never()).deleteById(any());
    }

    private Estoque criarEstoque() {
        Estoque estoque = new Estoque();
        Produto produto = new Produto();
        produto.setId(1L);
        estoque.setProduto(produto);
        estoque.setStatus(EstoqueStatus.LACRADO);
        estoque.setQuantidade(10);
        estoque.setPrecoCusto(BigDecimal.valueOf(5.0));
        estoque.setLimiteEstoqueBaixo(2);
        return estoque;
    }
}
