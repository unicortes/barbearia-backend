package br.org.unicortes.barbearia.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import br.org.unicortes.barbearia.models.Promocao;
import br.org.unicortes.barbearia.repositories.PromocaoRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PromocaoServiceTest {

    @InjectMocks
    private PromocaoService promocaoService;

    @Mock
    private PromocaoRepository promocaoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPromocao() {
        Promocao promocao1 = new Promocao();
        Promocao promocao2 = new Promocao();
        when(promocaoRepository.findAll()).thenReturn(Arrays.asList(promocao1, promocao2));

        List<Promocao> result = promocaoService.getAllPromocao();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetPromocaoById() {
        Long id = 1L;
        Promocao promocao = new Promocao();
        promocao.setId(id);
        when(promocaoRepository.findById(id)).thenReturn(Optional.of(promocao));

        Optional<Promocao> result = promocaoService.getPromocaoById(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    public void testSavePromocao() {
        Promocao promocao = new Promocao();
        when(promocaoRepository.save(promocao)).thenReturn(promocao);

        Promocao result = promocaoService.savePromocao(promocao);
        assertNotNull(result);
        verify(promocaoRepository, times(1)).save(promocao);
    }

    @Test
    public void testUpdatePromocao() {
        Long id = 1L;
        Promocao promocao = new Promocao();
        promocao.setId(id);
        when(promocaoRepository.save(promocao)).thenReturn(promocao);

        Promocao result = promocaoService.updatePromocao(id, promocao);
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(promocaoRepository, times(1)).save(promocao);
    }

    @Test
    public void testDeletePromocao() {
        Long id = 1L;
        doNothing().when(promocaoRepository).deleteById(id);

        promocaoService.deletePromocao(id);
        verify(promocaoRepository, times(1)).deleteById(id);
    }
}