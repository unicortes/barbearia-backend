package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.ClientNotFoundException;
import br.org.unicortes.barbearia.models.Client;
import br.org.unicortes.barbearia.repositories.ClientRepository;
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

public class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new Client(null, "Gustavo", "81971015135", LocalDate.now(),"gustavo@email.com");
        client.setId(1L);
    }

    @Test
    void testCreateClient() {
        when(clientRepository.save(client)).thenReturn(client);
        Client createdClient = clientService.createClient(client);

        assertNotNull(createdClient);
        assertEquals(client.getName(), createdClient.getName());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testUpdateClient() {
        Long id = 1L;
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);
        Client updatedClient = clientService.updateClient(id, client);

        assertNotNull(updatedClient);
        assertEquals(client.getId(), updatedClient.getId());
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testDeleteClient() {
        Long id = 1L;
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).delete(client);

        assertDoesNotThrow(() -> clientService.deleteClient(id));
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    void testListAllClients() {
        when(clientRepository.findAll()).thenReturn(Arrays.asList(client));
        List<Client> clients = clientService.listAllClients();

        assertNotNull(clients);
        assertEquals(1, clients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testUpdateClientThrowsExceptionWhenNotFound() {
        Long id = 1L;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.updateClient(id, client));
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(0)).save(client);
    }
}