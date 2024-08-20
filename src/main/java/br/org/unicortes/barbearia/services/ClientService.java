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
        if (clientRepository.existsById(client.getId())) {
            throw new ClientAlreadyExistsException(client.getName());
        }
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client clientAtualizado) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        client.setName(clientAtualizado.getName());
        client.setEmail(clientAtualizado.getEmail());
        client.setPhone(clientAtualizado.getPhone());
        client.setBirthday(clientAtualizado.getBirthday());
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        clientRepository.delete(client);
    }

    public List<Client> listAllClients() {
        return clientRepository.findAll();
    }
}