package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.exceptions.ResourceNotFoundException;
import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.repositories.BarbeiroRepository;
import br.org.unicortes.barbearia.services.BarbeiroService;
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
public class BarbeiroIntegrationTest {

    @Autowired
    private BarbeiroService barbeiroService;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    private Barbeiro barbeiro;

    @BeforeEach
    void setUp() {
        barbeiroRepository.deleteAll(); // Limpa o banco de dados antes de cada teste
        barbeiro = new Barbeiro(null, "João Silva", "joao@example.com", "1234567890", "12345678901", 1500.0, "Rua 1, Nº 10", LocalDate.now(), "09:00 - 18:00");
    }

    @Test
    void testCreateBarbeiro() {
        Barbeiro savedBarbeiro = barbeiroService.createBarbeiro(barbeiro);
        assertNotNull(savedBarbeiro);
        assertNotNull(savedBarbeiro.getBarbeiroId());
        assertEquals(barbeiro.getBarbeiroNome(), savedBarbeiro.getBarbeiroNome());

        Optional<Barbeiro> foundBarbeiro = barbeiroRepository.findById(savedBarbeiro.getBarbeiroId());
        assertTrue(foundBarbeiro.isPresent());
    }

    @Test
    void testUpdateBarbeiro() {
        Barbeiro savedBarbeiro = barbeiroService.createBarbeiro(barbeiro);
        savedBarbeiro.setBarbeiroNome("Carlos Souza");
        Barbeiro updatedBarbeiro = barbeiroService.updateBarbeiro(savedBarbeiro.getBarbeiroId(), savedBarbeiro);
        assertNotNull(updatedBarbeiro);
        assertEquals("Carlos Souza", updatedBarbeiro.getBarbeiroNome());

        Optional<Barbeiro> foundBarbeiro = barbeiroRepository.findById(updatedBarbeiro.getBarbeiroId());
        assertTrue(foundBarbeiro.isPresent());
        assertEquals("Carlos Souza", foundBarbeiro.get().getBarbeiroNome());
    }

    @Test
    void testDeleteBarbeiro() {
        Barbeiro savedBarbeiro = barbeiroService.createBarbeiro(barbeiro);
        barbeiroService.deleteBarbeiro(savedBarbeiro.getBarbeiroId());
        Optional<Barbeiro> foundBarbeiro = barbeiroRepository.findById(savedBarbeiro.getBarbeiroId());
        assertFalse(foundBarbeiro.isPresent());
    }

    @Test
    void testListarTodosBarbeiros() {
        Barbeiro barbeiro1 = barbeiroService.createBarbeiro(barbeiro);
        Barbeiro barbeiro2 = barbeiroService.createBarbeiro(new Barbeiro(null, "Carlos Souza", "carlos@example.com", "0987654321", "10987654321", 1600.0, "Rua 2, Nº 20", LocalDate.now(), "10:00 - 19:00"));
        List<Barbeiro> barbeiros = barbeiroService.listarTodosBarbeiros();

        assertNotNull(barbeiros);
        assertEquals(2, barbeiros.size());
        assertTrue(barbeiros.contains(barbeiro1));
        assertTrue(barbeiros.contains(barbeiro2));
    }

    @Test
    void testUpdateBarbeiroThrowsExceptionWhenNotFound() {
        Barbeiro nonExistentBarbeiro = new Barbeiro(null, "Pedro Mendes", "pedro@example.com", "1111111111", "22222222222", 1700.0, "Rua 3, Nº 30", LocalDate.now(), "11:00 - 20:00");
        nonExistentBarbeiro.setBarbeiroId(99L);

        assertThrows(ResourceNotFoundException.class, () -> barbeiroService.updateBarbeiro(nonExistentBarbeiro.getBarbeiroId(), nonExistentBarbeiro));
    }
}
