package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.exceptions.ResourceNotFoundException;
import br.org.unicortes.barbearia.models.Barber;
import br.org.unicortes.barbearia.repositories.BarberRepository;
import br.org.unicortes.barbearia.services.BarberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BarberIntegrationTest {

    @Autowired
    private BarberService barberService;

    @Autowired
    private BarberRepository barberRepository;

    private Barber barber;

    @BeforeEach
    void setUp() {
        barberRepository.deleteAll(); // Limpa o banco de dados antes de cada teste
        barber = new Barber(null, "João Silva", "joao@example.com", "1234567890", "12345678901", 1500.0, "Rua 1, Nº 10", LocalDate.now(), "09:00 - 18:00");
    }

    @Test
    void testCreateBarber() {
        Barber savedBarber = barberService.createBarber(barber);
        assertNotNull(savedBarber);
        assertNotNull(savedBarber.getId());
        assertEquals(barber.getName(), savedBarber.getName());

        Optional<Barber> foundBarber = barberRepository.findById(savedBarber.getId());
        assertTrue(foundBarber.isPresent());
    }
    
    @Test
    void testUpdateBarber() {
        Barber savedBarber = barberService.createBarber(barber);
        savedBarber.setName("Carlos Souza");
        Barber updatedBarber = barberService.updateBarber(savedBarber.getId(), savedBarber);
        assertNotNull(updatedBarber);
        assertEquals("Carlos Souza", updatedBarber.getName());

        Optional<Barber> foundBarber = barberRepository.findById(updatedBarber.getId());
        assertTrue(foundBarber.isPresent());
        assertEquals("Carlos Souza", foundBarber.get().getName());
    }

    @Test
    void testDeleteBarbeiro() {
        Barber savedBarber = barberService.createBarber(barber);
        barberService.deleteBarber(savedBarber.getId());
        Optional<Barber> foundBarber = barberRepository.findById(savedBarber.getId());
        assertFalse(foundBarber.isPresent());
    }

    @Test
    void testListAllBarbers() {
        Barber barbeiro1 = barberService.createBarber(barber);
        Barber barbeiro2 = barberService.createBarber(new Barber(null, "Carlos Souza", "carlos@example.com", "0987654321", "10987654321", 1600.0, "Rua 2, Nº 20", LocalDate.now(), "10:00 - 19:00"));
        List<Barber> barbers = barberService.listAllBarbers();

        assertNotNull(barbers);
        assertEquals(2, barbers.size());
        assertTrue(barbers.contains(barbeiro1));
        assertTrue(barbers.contains(barbeiro2));
    }

    @Test
    void testUpdateBarberThrowsExceptionWhenNotFound() {
        Barber nonExistentBarber = new Barber(null, "Pedro Mendes", "pedro@example.com", "1111111111", "22222222222", 1700.0, "Rua 3, Nº 30", LocalDate.now(), "11:00 - 20:00");
        nonExistentBarber.setId(99L);

        assertThrows(ResourceNotFoundException.class, () -> barberService.updateBarber(nonExistentBarber.getId(), nonExistentBarber));
    }
}