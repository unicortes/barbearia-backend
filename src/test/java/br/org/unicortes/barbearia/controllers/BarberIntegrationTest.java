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

        // Inicialize o objeto barber com gets e sets
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

    @Test
    void testCreateBarber() {
        // Crie um objeto Barber com todos os campos obrigatórios preenchidos
        Barber barber = new Barber();
        barber.setName("João Silva");
        barber.setCpf("12345678901");
        barber.setAddress("Rua 1, Nº 10");
        barber.setAdmissionDate(LocalDate.of(2024, 8, 20));
        barber.setEmail("joao@example.com");
        barber.setOpeningHours("09:00 - 18:00");
        barber.setPhone("1234567890");
        barber.setSalary(1500.00);

        // Salva o Barber
        Barber savedBarber = barberService.createBarber(barber);

        // Verifique se o Barber foi salvo corretamente
        assertNotNull(savedBarber);
        assertNotNull(savedBarber.getId());
        assertEquals(barber.getName(), savedBarber.getName());

        // Verifique se o Barber foi encontrado no repositório
        Optional<Barber> foundBarber = barberRepository.findById(savedBarber.getId());
        assertTrue(foundBarber.isPresent());
        assertEquals(barber.getCpf(), foundBarber.get().getCpf());
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
        Barber barbeiro2 = barberService.createBarber(new Barber());
        List<Barber> barbers = barberService.listAllBarbers();

        assertNotNull(barbers);
        assertEquals(2, barbers.size());
        assertTrue(barbers.contains(barbeiro1));
        assertTrue(barbers.contains(barbeiro2));
    }

    @Test
    void testUpdateBarberThrowsExceptionWhenNotFound() {
        Barber nonExistentBarber = new Barber();
        nonExistentBarber.setId(99L);

        assertThrows(ResourceNotFoundException.class, () -> barberService.updateBarber(nonExistentBarber.getId(), nonExistentBarber));
    }
}