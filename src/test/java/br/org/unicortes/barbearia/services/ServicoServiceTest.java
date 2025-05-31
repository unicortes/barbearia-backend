package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicoServiceTest {

    @Mock
    private ServicoRepository servicoRepository;

    @InjectMocks
    private ServicoService servicoService;

    private Servico servico;

    @BeforeEach
    void setup() {
        servico = new Servico();
        servico.setId(1L);
        servico.setNome("Corte de Cabelo");
        servico.setDescricao("Corte masculino");
        servico.setPreco(new BigDecimal("50.00"));
        servico.setDuracaoPadraoMinutos(30);
        servico.setAtivo(true);
    }

    @Test
    void listarTodos() {
        when(servicoRepository.findAll()).thenReturn(List.of(servico));

        List<Servico> resultado = servicoService.listarTodos();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(servicoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_Existe() {
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));

        Servico resultado = servicoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(servico.getNome(), resultado.getNome());
        verify(servicoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_NaoExiste() {
        when(servicoRepository.findById(1L)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class,
                () -> servicoService.buscarPorId(1L));

        assertEquals("Serviço não encontrado", exception.getMessage());
        verify(servicoRepository, times(1)).findById(1L);
    }

    @Test
    void criarSucesso() {
        when(servicoRepository.save(servico)).thenReturn(servico);

        Servico resultado = servicoService.criar(servico);

        assertNotNull(resultado);
        assertEquals(servico.getNome(), resultado.getNome());
        verify(servicoRepository, times(1)).save(servico);
    }

    @Test
    void atualizarSucesso() {
        Servico servicoAtualizado = new Servico();
        servicoAtualizado.setNome("Corte Premium");
        servicoAtualizado.setDescricao("Corte masculino com estilo");
        servicoAtualizado.setPreco(new BigDecimal("70.00"));
        servicoAtualizado.setDuracaoPadraoMinutos(40);
        servicoAtualizado.setAtivo(false);

        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
        when(servicoRepository.save(any(Servico.class))).thenAnswer(i -> i.getArgument(0));

        Servico resultado = servicoService.atualizar(1L, servicoAtualizado);

        assertNotNull(resultado);
        assertEquals("Corte Premium", resultado.getNome());
        assertEquals("Corte masculino com estilo", resultado.getDescricao());
        assertEquals(new BigDecimal("70.00"), resultado.getPreco());
        assertEquals(40, resultado.getDuracaoPadraoMinutos());
        assertTrue(resultado.isAtivo());

        verify(servicoRepository, times(1)).findById(1L);
        verify(servicoRepository, times(1)).save(resultado);
    }

    @Test
    void atualizarErro() {
        Servico servicoAtualizado = new Servico();

        when(servicoRepository.findById(1L)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class,
                () -> servicoService.atualizar(1L, servicoAtualizado));

        assertEquals("Serviço não encontrado", exception.getMessage());
        verify(servicoRepository, times(1)).findById(1L);
        verify(servicoRepository, never()).save(any());
    }

    @Test
    void removerSucesso() {
        when(servicoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(servicoRepository).deleteById(1L);

        assertDoesNotThrow(() -> servicoService.remover(1L));

        verify(servicoRepository, times(1)).existsById(1L);
        verify(servicoRepository, times(1)).deleteById(1L);
    }

    @Test
    void removerErro() {
        when(servicoRepository.existsById(1L)).thenReturn(false);

        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class,
                () -> servicoService.remover(1L));

        assertEquals("Serviço não encontrado", exception.getMessage());
        verify(servicoRepository, times(1)).existsById(1L);
        verify(servicoRepository, never()).deleteById(anyLong());
    }
}
