package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.ResourceNotFoundException;
import br.org.unicortes.barbearia.models.Client;
import br.org.unicortes.barbearia.models.LoyaltyCard;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.ClientRepository;
import br.org.unicortes.barbearia.repositories.LoyaltyCardRepository;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoyaltyCardServiceTest {

    // Mock do repositório de cartões de fidelidade
    @Mock
    private LoyaltyCardRepository loyaltyCardRepository;

    // Mock do repositório de clientes
    @Mock
    private ClientRepository clientRepository;

    // Mock do repositório de serviços
    @Mock
    private ServicoRepository servicoRepository;

    // Serviço de cartões de fidelidade a ser testado
    @InjectMocks
    private LoyaltyCardService loyaltyCardService;

    // Instâncias para uso nos testes
    private LoyaltyCard loyaltyCard;
    private Client client;
    private Servico service;

    @BeforeEach
    void setUp() {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this);

        // Configura o cliente de teste
        client = new Client();
        client.setId(1L);

        // Configura o serviço de teste
        service = new Servico();
        service.setServicoId(1L);

        // Configura o cartão de fidelidade de teste
        loyaltyCard = new LoyaltyCard();
        loyaltyCard.setId(1L);
        loyaltyCard.setClient(client);
        loyaltyCard.setService(service);
        loyaltyCard.setDateAdmission(new java.util.Date());
        loyaltyCard.setPoints(10);
    }

    @Test
    void testCreateLoyaltyCard() {
        // Configura o comportamento esperado dos mocks
        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
        when(servicoRepository.findById(service.getServicoId())).thenReturn(Optional.of(service));
        when(loyaltyCardRepository.save(loyaltyCard)).thenReturn(loyaltyCard);

        // Chama o meodo a ser testado
        LoyaltyCard createdLoyaltyCard = loyaltyCardService.createLoyaltyCard(loyaltyCard);

        // Verifica se o cartão de fidelidade foi criado corretamente
        assertNotNull(createdLoyaltyCard);
        assertEquals(loyaltyCard.getClient().getId(), createdLoyaltyCard.getClient().getId());
        assertEquals(loyaltyCard.getService().getServicoId(), createdLoyaltyCard.getService().getServicoId());

        // Verifica se os métodos dos mocks foram chamados com a quantidade esperada
        verify(clientRepository, times(1)).findById(client.getId());
        verify(servicoRepository, times(1)).findById(service.getServicoId());
        verify(loyaltyCardRepository, times(1)).save(loyaltyCard);
    }

    @Test
    void testUpdateLoyaltyCard() {
        Long id = 1L;
        // Configura o comportamento esperado dos mocks
        when(loyaltyCardRepository.findById(id)).thenReturn(Optional.of(loyaltyCard));
        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
        when(servicoRepository.findById(service.getServicoId())).thenReturn(Optional.of(service));
        when(loyaltyCardRepository.save(loyaltyCard)).thenReturn(loyaltyCard);

        // Chama o metodo a ser testado
        LoyaltyCard updatedLoyaltyCard = loyaltyCardService.updateLoyaltyCard(id, loyaltyCard);

        // Verifica se o cartão de fidelidade foi atualizado corretamente
        assertNotNull(updatedLoyaltyCard);
        assertEquals(loyaltyCard.getId(), updatedLoyaltyCard.getId());

        // Verifica se os métodos dos mocks foram chamados com a quantidade esperada
        verify(loyaltyCardRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).findById(client.getId());
        verify(servicoRepository, times(1)).findById(service.getServicoId());
        verify(loyaltyCardRepository, times(1)).save(loyaltyCard);
    }

    @Test
    void testDeleteLoyaltyCard() {
        Long id = 1L;
        // Configura o comportamento esperado dos mocks
        when(loyaltyCardRepository.findById(id)).thenReturn(Optional.of(loyaltyCard));
        doNothing().when(loyaltyCardRepository).delete(loyaltyCard);

        // Chama o metodo a ser testado e verifica se não ocorre exceção
        assertDoesNotThrow(() -> loyaltyCardService.deleteLoyaltyCard(id));

        // Verifica se os métodos dos mocks foram chamados com a quantidade esperada
        verify(loyaltyCardRepository, times(1)).findById(id);
        verify(loyaltyCardRepository, times(1)).delete(loyaltyCard);
    }

    @Test
    void testListAllLoyaltyCards() {
        // Configura o comportamento esperado dos mocks
        when(loyaltyCardRepository.findAll()).thenReturn(Arrays.asList(loyaltyCard));

        // Chama o metodo a ser testado
        List<LoyaltyCard> loyaltyCards = loyaltyCardService.getAllLoyaltyCards();

        // Verifica se a lista de cartões de fidelidade é retornada corretamente
        assertNotNull(loyaltyCards);
        assertEquals(1, loyaltyCards.size());

        // Verifica se o metodo dos mocks foi chamado com a quantidade esperada
        verify(loyaltyCardRepository, times(1)).findAll();
    }

    @Test
    void testGetLoyaltyCardByIdThrowsExceptionWhenNotFound() {
        Long id = 1L;
        // Configura o comportamento esperado dos mocks para quando o cartão não é encontrado
        when(loyaltyCardRepository.findById(id)).thenReturn(Optional.empty());

        // Verifica se a exceção é lançada quando o cartão de fidelidade não é encontrado
        assertThrows(ResourceNotFoundException.class, () -> loyaltyCardService.getLoyaltyCardById(id));

        // Verifica se o metodo dos mocks foi chamado com a quantidade esperada
        verify(loyaltyCardRepository, times(1)).findById(id);
    }
}
