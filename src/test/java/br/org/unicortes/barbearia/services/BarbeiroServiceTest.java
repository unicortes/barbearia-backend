package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.exceptions.ResourceNotFoundException;
import br.org.unicortes.barbearia.repositories.BarbeiroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BarbeiroServiceTest {

    @InjectMocks
    private BarbeiroService barbeiroService;

    @Mock
    private BarbeiroRepository barbeiroRepository;

    private Barbeiro barbeiro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        barbeiro = new Barbeiro("Gustavo", "gustavo@email.com", "1234567890", "12345678901", 1500.0, "Rua 1, Nº 10", LocalDate.now(), "09:00 - 18:00");
        barbeiro.setBarbeiroById(1L);
    }

    // teste 1
    @Test
    void testCreateBarbeiro() {
        when(barbeiroRepository.save(barbeiro)).thenReturn(barbeiro);
        Barbeiro createdBarbeiro = barbeiroService.createBarbeiro(barbeiro);

        assertNotNull(createdBarbeiro);
        assertEquals(barbeiro.getBarbeiroByNome(), createdBarbeiro.getBarbeiroByNome());
        verify(barbeiroRepository, times(1)).save(barbeiro);
    }

    // teste 2
    @Test
    void testUpdateBarbeiro() {
        Long id = 1L;
        when(barbeiroRepository.findById(id)).thenReturn(Optional.of(barbeiro));
        when(barbeiroRepository.save(barbeiro)).thenReturn(barbeiro);
        Barbeiro updatedBarbeiro = barbeiroService.updateBarbeiro(id, barbeiro);

        assertNotNull(updatedBarbeiro);
        assertEquals(barbeiro.getBarbeiroById(), updatedBarbeiro.getBarbeiroById());
        verify(barbeiroRepository, times(1)).findById(id);
        verify(barbeiroRepository, times(1)).save(barbeiro);
    }

    // teste 3
    @Test
    void testDeleteBarbeiro() {
        Long id = 1L;
        when(barbeiroRepository.findById(id)).thenReturn(Optional.of(barbeiro));
        doNothing().when(barbeiroRepository).delete(barbeiro);

        assertDoesNotThrow(() -> barbeiroService.deleteBarbeiro(id));
        verify(barbeiroRepository, times(1)).findById(id);
        verify(barbeiroRepository, times(1)).delete(barbeiro);
    }

    // teste 4
    @Test
    void testListarTodosBarbeiros() {
        when(barbeiroRepository.findAll()).thenReturn(Arrays.asList(barbeiro));
        List<Barbeiro> barbeiros = barbeiroService.listarTodosBarbeiros();

        assertNotNull(barbeiros);
        assertEquals(1, barbeiros.size());
        verify(barbeiroRepository, times(1)).findAll();
    }

    // teste 5 - terminar
    @Test
    void testUpdateBarbeiroThrowsExceptionWhenNotFound() {
        Long id = 1L;
        when(barbeiroRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> barbeiroService.updateBarbeiro(id, barbeiro));
        verify(barbeiroRepository, times(1)).findById(id);
        verify(barbeiroRepository, times(0)).save(barbeiro);
    }
}

