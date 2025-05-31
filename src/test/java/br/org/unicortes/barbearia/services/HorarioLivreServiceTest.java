package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.models.HorarioLivre;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.HorarioLivreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HorarioLivreServiceTest {

    @Mock
    private HorarioLivreRepository repository;

    @InjectMocks
    private HorarioLivreService service;

    private HorarioLivre horarioLivre;

    @BeforeEach
    void setUp() {
        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setId(1L);

        Servico servico = new Servico();
        servico.setId(1L);

        horarioLivre = new HorarioLivre();
        horarioLivre.setId(1L);
        horarioLivre.setBarbeiro(barbeiro);
        horarioLivre.setServico(servico);
        horarioLivre.setInicio(LocalDateTime.of(2025, 5, 1, 10, 0));
        horarioLivre.setFim(LocalDateTime.of(2025, 5, 1, 11, 0));
        horarioLivre.setDisponivel(true);
    }

    @Test
    void listarTodos() {
        when(repository.findAll()).thenReturn(List.of(horarioLivre));
        List<HorarioLivre> result = service.listarTodos();
        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void buscarPorIdExistente() {
        when(repository.findById(1L)).thenReturn(Optional.of(horarioLivre));
        HorarioLivre result = service.buscarPorId(1L);
        assertEquals(horarioLivre, result);
        verify(repository).findById(1L);
    }

    @Test
    void buscarPorIdNaoExistente() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class,
                () -> service.buscarPorId(2L));
        assertTrue(exception.getMessage().contains("Horário livre não encontrado"));
    }

    @Test
    void criarSemConflito() {
        when(repository.findConflictingSchedules(anyLong(), any(), any(), anyLong()))
                .thenReturn(Collections.emptyList());
        when(repository.save(horarioLivre)).thenReturn(horarioLivre);

        HorarioLivre criado = service.criar(horarioLivre);

        assertEquals(horarioLivre, criado);
        verify(repository).save(horarioLivre);
    }

    @Test
    void criarComConflito() {
        when(repository.findConflictingSchedules(anyLong(), any(), any(), anyLong()))
                .thenReturn(List.of(new HorarioLivre()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.criar(horarioLivre));
        assertTrue(ex.getMessage().contains("conflita com o intervalo"));
    }

    @Test
    void atualizarComSucesso() {
        HorarioLivre atualizado = new HorarioLivre();
        atualizado.setBarbeiro(horarioLivre.getBarbeiro());
        atualizado.setServico(horarioLivre.getServico());
        atualizado.setInicio(LocalDateTime.of(2025, 5, 1, 12, 0));
        atualizado.setFim(LocalDateTime.of(2025, 5, 1, 13, 0));
        atualizado.setDisponivel(false);

        when(repository.findById(1L)).thenReturn(Optional.of(horarioLivre));
        when(repository.findConflictingSchedules(anyLong(), any(), any(), anyLong()))
                .thenReturn(Collections.emptyList());
        when(repository.save(any())).thenReturn(horarioLivre);

        HorarioLivre result = service.atualizar(1L, atualizado);

        assertEquals(atualizado.getInicio(), result.getInicio());
        assertEquals(atualizado.getFim(), result.getFim());
        assertFalse(result.isDisponivel());
        verify(repository).save(horarioLivre);
    }

    @Test
    void removerComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(horarioLivre));
        doNothing().when(repository).delete(horarioLivre);

        assertDoesNotThrow(() -> service.remover(1L));
        verify(repository).delete(horarioLivre);
    }

    @Test
    void removerNaoExistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntidadeNaoEncontradaException.class, () -> service.remover(99L));
    }
}
