package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.ConflitoException;
import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.repositories.BarbeiroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarbeiroServiceTest {

    @Mock
    private BarbeiroRepository barbeiroRepository;

    @InjectMocks
    private BarbeiroService barbeiroService;

    private Barbeiro barbeiro;

    @BeforeEach
    void setUp() {
        barbeiro = new Barbeiro();
        barbeiro.setId(1L);
        barbeiro.setName("João");
        barbeiro.setEmail("joao@example.com");
        barbeiro.setTelefone("123456789");
        barbeiro.setCpf("11122233344");
        barbeiro.setSalario(BigDecimal.valueOf(2000));
        barbeiro.setEndereco("Rua A, 123");
        barbeiro.setDataAdmissao(LocalDate.of(2022, 1, 1));
        barbeiro.setDataDemissao(null);
        barbeiro.setEspecialidades("Corte, Barba");
        barbeiro.setAtivo(true);
    }

    @Test
    void listarTodos() {
        when(barbeiroRepository.findAll()).thenReturn(List.of(barbeiro));
        List<Barbeiro> result = barbeiroService.listarTodos();
        assertEquals(1, result.size());
        verify(barbeiroRepository).findAll();
    }

    @Test
    void buscarPorIdExistente() {
        Long barbeiroId = 1L;
        doReturn(Optional.of(barbeiro)).when(barbeiroRepository).findById(barbeiroId);
        Barbeiro result = barbeiroService.buscarPorId(barbeiroId);
        assertEquals(barbeiro, result);
    }

    @Test
    void buscarPorIdInexistente() {
        Long barbeiroId = 1L;
        doReturn(Optional.empty()).when(barbeiroRepository).findById(barbeiroId);
        assertThrows(EntidadeNaoEncontradaException.class, () -> barbeiroService.buscarPorId(barbeiroId));
    }

    @Test
    void criarBarbeiro() {
        when(barbeiroRepository.existsByEmail(barbeiro.getEmail())).thenReturn(false);
        when(barbeiroRepository.existsByTelefone(barbeiro.getTelefone())).thenReturn(false);
        when(barbeiroRepository.existsByCpf(barbeiro.getCpf())).thenReturn(false);
        when(barbeiroRepository.save(barbeiro)).thenReturn(barbeiro);

        Barbeiro result = barbeiroService.criar(barbeiro);

        assertNotNull(result);
        assertEquals("João", result.getName());
        verify(barbeiroRepository).save(barbeiro);
    }

    @Test
    void criarBarbeiroErroEmail() {
        when(barbeiroRepository.existsByEmail(barbeiro.getEmail())).thenReturn(true);
        assertThrows(ConflitoException.class, () -> barbeiroService.criar(barbeiro));
        verify(barbeiroRepository).existsByEmail(barbeiro.getEmail());
        verify(barbeiroRepository, never()).save(any());
    }

    @Test
    void criarBarbeiroErroTelefone() {
        when(barbeiroRepository.existsByEmail(barbeiro.getEmail())).thenReturn(false);
        when(barbeiroRepository.existsByTelefone(barbeiro.getTelefone())).thenReturn(true);
        assertThrows(ConflitoException.class, () -> barbeiroService.criar(barbeiro));
    }

    @Test
    void criarBarbeiroErroCpf() {
        when(barbeiroRepository.existsByEmail(barbeiro.getEmail())).thenReturn(false);
        when(barbeiroRepository.existsByTelefone(barbeiro.getTelefone())).thenReturn(false);
        when(barbeiroRepository.existsByCpf(barbeiro.getCpf())).thenReturn(true);
        assertThrows(ConflitoException.class, () -> barbeiroService.criar(barbeiro));
    }

    @Test
    void atualizarBarbeiro() {
        Barbeiro atualizado = new Barbeiro();
        atualizado.setName("João Atualizado");
        atualizado.setEmail("novo@email.com");
        atualizado.setTelefone("11888888888");
        atualizado.setDataAdmissao(LocalDate.of(1995, 2, 2));
        atualizado.setDataDemissao(null);
        atualizado.setEspecialidades("Corte, Barba");
        atualizado.setAtivo(false);

        Long barbeiroId = 1L;

        when(barbeiroRepository.findById(barbeiroId)).thenReturn(Optional.of(barbeiro));
        when(barbeiroRepository.existsByEmailAndIdNot(atualizado.getEmail(), barbeiroId)).thenReturn(false);
        when(barbeiroRepository.existsByTelefoneAndIdNot(atualizado.getTelefone(), barbeiroId)).thenReturn(false);
        when(barbeiroRepository.save(any(Barbeiro.class))).thenAnswer(i -> i.getArgument(0));

        Barbeiro result = barbeiroService.atualizar(barbeiroId, atualizado);

        assertEquals("João Atualizado", result.getName());
        assertEquals("novo@email.com", result.getEmail());
        assertEquals("11888888888", result.getTelefone());
        assertFalse(result.isAtivo());

        verify(barbeiroRepository).save(any(Barbeiro.class));
    }

    @Test
    void removerBarbeiroExistente() {
        when(barbeiroRepository.existsById(1L)).thenReturn(true);
        doNothing().when(barbeiroRepository).deleteById(1L);

        assertDoesNotThrow(() -> barbeiroService.remover(1L));
        verify(barbeiroRepository).existsById(1L);
        verify(barbeiroRepository).deleteById(1L);
    }

    @Test
    void removerBarbeiroInexistente() {
        when(barbeiroRepository.existsById(1L)).thenReturn(false);
        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class, () -> barbeiroService.remover(1L));
        assertEquals("Barbeiro não encontrado com id: 1", ex.getMessage());
        verify(barbeiroRepository).existsById(1L);
        verify(barbeiroRepository, never()).deleteById(any());
    }

    @Test
    void ativarBarbeiro() {
        barbeiro.setAtivo(false);
        when(barbeiroRepository.findById(barbeiro.getId())).thenReturn(Optional.of(barbeiro));
        barbeiroService.ativarBarbeiro(1L);

        assertTrue(barbeiro.isAtivo());
        verify(barbeiroRepository).save(barbeiro);
    }

    @Test
    void desativarBarbeiro() {
        Long barbeiroId = 1L;
        barbeiro.setAtivo(true);

        doReturn(Optional.of(barbeiro)).when(barbeiroRepository).findById(barbeiroId);

        when(barbeiroRepository.save(any(Barbeiro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        barbeiroService.desativarBarbeiro(barbeiroId);

        assertFalse(barbeiro.isAtivo());
        verify(barbeiroRepository).save(barbeiro);
    }
}
