package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.Promocao;
import br.org.unicortes.barbearia.repositories.PromocaoRepository;
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
class PromocaoServiceTest {

    @InjectMocks
    private PromocaoService promocaoService;

    @Mock
    private PromocaoRepository promocaoRepository;

    private Promocao criarPromocaoMock() {
        Promocao p = new Promocao();
        p.setId(1L);
        p.setNome("Promo Corte Masculino");
        p.setDescricao("Corte com 20% de desconto");
        p.setCodigoPromo("CORTE20");
        p.setCategoria("Corte");
        p.setDesconto(20.0);
        p.setDisponibilidade(true);
        p.setInicio(LocalDate.now());
        p.setFim(LocalDate.now().plusDays(10));
        return p;
    }

    @Test
    void listarTodosSucesso() {
        List<Promocao> lista = List.of(criarPromocaoMock());
        when(promocaoRepository.findAll()).thenReturn(lista);

        List<Promocao> resultado = promocaoService.listarTodos();

        assertEquals(1, resultado.size());
        verify(promocaoRepository).findAll();
    }

    @Test
    void buscarPorIdSucesso() {
        Promocao mock = criarPromocaoMock();
        when(promocaoRepository.findById(1L)).thenReturn(Optional.of(mock));

        Promocao resultado = promocaoService.buscarPorId(1L);

        assertEquals("Promo Corte Masculino", resultado.getNome());
        verify(promocaoRepository).findById(1L);
    }

    @Test
    void buscarPorIdErro() {
        when(promocaoRepository.findById(999L)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException ex = assertThrows(
                EntidadeNaoEncontradaException.class,
                () -> promocaoService.buscarPorId(999L)
        );

        assertEquals("Promoção não encontrada", ex.getMessage());
    }

    @Test
    void criarSucesso() {
        Promocao nova = criarPromocaoMock();
        when(promocaoRepository.save(nova)).thenReturn(nova);

        Promocao resultado = promocaoService.criar(nova);

        assertEquals("CORTE20", resultado.getCodigoPromo());
        verify(promocaoRepository).save(nova);
    }

    @Test
    void atualizarSucesso() {
        Promocao existente = criarPromocaoMock();
        Promocao atualizada = criarPromocaoMock();
        atualizada.setNome("Promo Corte Atualizada");
        atualizada.setDisponibilidade(false);
        atualizada.setDesconto(15.0);

        when(promocaoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(promocaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Promocao resultado = promocaoService.atualizar(1L, atualizada);

        assertEquals("Promo Corte Atualizada", resultado.getNome());
        assertFalse(resultado.isDisponibilidade());
        assertEquals(15.0, resultado.getDesconto());
        verify(promocaoRepository).save(existente);
    }

    @Test
    void atualizarErro() {
        Promocao atualizada = criarPromocaoMock();
        when(promocaoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class,
                () -> promocaoService.atualizar(999L, atualizada));
    }

    @Test
    void removerSucesso() {
        when(promocaoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(promocaoRepository).deleteById(1L);

        promocaoService.remover(1L);

        verify(promocaoRepository).deleteById(1L);
    }

    @Test
    void removerErro() {
        when(promocaoRepository.existsById(999L)).thenReturn(false);

        EntidadeNaoEncontradaException ex = assertThrows(
                EntidadeNaoEncontradaException.class,
                () -> promocaoService.remover(999L)
        );

        assertEquals("Promoção não encontrada", ex.getMessage());
    }
}
