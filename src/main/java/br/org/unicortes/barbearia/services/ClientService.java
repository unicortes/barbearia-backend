package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Client;
import br.org.unicortes.barbearia.exceptions.ClientAlreadyExistsException;
import br.org.unicortes.barbearia.exceptions.ClientNotFoundException;
import br.org.unicortes.barbearia.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public Client createClient(Client client) {
        if (clientRepository.existsByName(client.getName())) {
            throw new ClientAlreadyExistsException(client.getName());
        }
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        client.setName(clientDetails.getName());
        client.setEmail(clientDetails.getEmail());
        client.setPhone(clientDetails.getPhone());
        client.setBirthday(clientDetails.getBirthday());
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        clientRepository.delete(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }
}