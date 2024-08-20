package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Barber;
import br.org.unicortes.barbearia.exceptions.ResourceNotFoundException;
import br.org.unicortes.barbearia.repositories.BarberRepository;
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

public class BarberServiceTest {

    @InjectMocks
    private BarberService barberService;

    @Mock
    private BarberRepository barberRepository;

    private Barber barber;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        barber = new Barber();
        barber.setId(1L);
        barber.setName("João Silva");
        barber.setEmail("joao@example.com");
        barber.setPhone("1234567890");
        barber.setCpf("12345678901");
        barber.setSalary(1500.0);
        barber.setAddress("Rua 1, Nº 10");
        barber.setAdmissionDate(LocalDate.now());
        barber.setOpeningHours("09:00 - 18:00");
    }

    // teste 1
    @Test
    void testCreateBarber() {
        when(barberRepository.save(barber)).thenReturn(barber);
        Barber createdBarber = barberService.createBarber(barber);

        assertNotNull(createdBarber);
        assertEquals(barber.getName(), createdBarber.getName());
        verify(barberRepository, times(1)).save(barber);
    }

    // teste 2
    @Test
    void testUpdateBarber() {
        Long id = 1L;
        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));
        when(barberRepository.save(barber)).thenReturn(barber);
        Barber updatedBarber = barberService.updateBarber(id, barber);

        assertNotNull(updatedBarber);
        assertEquals(barber.getId(), updatedBarber.getId());
        verify(barberRepository, times(1)).findById(id);
        verify(barberRepository, times(1)).save(barber);
    }

    // teste 3
    @Test
    void testDeleteBarber() {
        Long id = 1L;
        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));
        doNothing().when(barberRepository).delete(barber);

        assertDoesNotThrow(() -> barberService.deleteBarber(id));
        verify(barberRepository, times(1)).findById(id);
        verify(barberRepository, times(1)).delete(barber);
    }

    // teste 4
    @Test
    void testListAllBarbers() {
        when(barberRepository.findAll()).thenReturn(Arrays.asList(barber));
        List<Barber> barbers = barberService.listAllBarbers();

        assertNotNull(barbers);
        assertEquals(1, barbers.size());
        verify(barberRepository, times(1)).findAll();
    }

    // teste 5 - terminar
    @Test
    void testUpdateBarberThrowsExceptionWhenNotFound() {
        Long id = 1L;
        when(barberRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> barberService.updateBarber(id, barber));
        verify(barberRepository, times(1)).findById(id);
        verify(barberRepository, times(0)).save(barber);
    }
}