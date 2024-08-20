package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.exceptions.ClientNotFoundException;
import br.org.unicortes.barbearia.models.Client;
import br.org.unicortes.barbearia.repositories.ClientRepository;
import br.org.unicortes.barbearia.services.ClientService;
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
public class ClientIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll(); // Limpa o banco de dados antes de cada teste
        client = new Client(1L, "Gustavo", "81971015135", LocalDate.now(),"gustavo@email.com");
    }

    @Test
    void testCreateClient() {
        Client savedClient = clientService.createClient(client);
        assertNotNull(savedClient);
        assertNotNull(savedClient.getId());
        assertEquals(client.getName(), savedClient.getName());

        Optional<Client> foundClient = clientRepository.findById(savedClient.getId());
        assertTrue(foundClient.isPresent());
    }
    
    @Test
    void testUpdateClient() {
        Client savedClient = clientService.createClient(client);
        savedClient.setName("Carlos Souza");
        Client updatedClient = clientService.updateClient(savedClient.getId(), savedClient);
        assertNotNull(updatedClient);
        assertEquals("Carlos Souza", updatedClient.getName());

        Optional<Client> foundClient = clientRepository.findById(updatedClient.getId());
        assertTrue(foundClient.isPresent());
        assertEquals("Carlos Souza", foundClient.get().getName());
    }

    @Test
    void testDeleteClient() {
        Client savedClient = clientService.createClient(client);
        clientService.deleteClient(savedClient.getId());
        Optional<Client> foundClient = clientRepository.findById(savedClient.getId());
        assertFalse(foundClient.isPresent());
    }

    @Test
    void testListAllClients() {
        Client client1 = clientService.createClient(client);
        Client client2 = clientService.createClient(new Client(2L, "Matheus", "81971015135", LocalDate.now(),"gustavo@email.com"));
        List<Client> clients = clientService.listAllClients();

        assertNotNull(clients);
        assertEquals(2, clients.size());
        assertTrue(clients.contains(client1));
        assertTrue(clients.contains(client2));
    }

    @Test
    void testUpdateClientThrowsExceptionWhenNotFound() {
        Client nonExistentClient = new Client(4L, "Matheus", "81971015135", LocalDate.now(),"gustavo@email.com");
        nonExistentClient.setId(99L);

        assertThrows(ClientNotFoundException.class, () -> clientService.updateClient(nonExistentClient.getId(), nonExistentClient));
    }
}