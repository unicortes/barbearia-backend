package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.Client;
import br.org.unicortes.barbearia.dtos.ClientDTO;
import br.org.unicortes.barbearia.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO){
        Client client = convertToEntity(clientDTO);
        Client newClient = clientService.createClient(client);
        return ResponseEntity.ok(convertToDTO(newClient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        Client client = convertToEntity(clientDTO);
        Client clientAtualizado = clientService.updateClient(id, client);
        return ResponseEntity.ok(convertToDTO(clientAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Client>> listClient() {
        List<Client> clients = clientService.listAllClients();
        return ResponseEntity.ok(clients);
    }

    private ClientDTO convertToDTO(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setName(client.getName());
        clientDTO.setEmail(client.getEmail());
        clientDTO.setPhone(client.getPhone());
        clientDTO.setBirthday(client.getBirthday());
        return clientDTO;
    }

    private Client convertToEntity(ClientDTO clientDTO) {
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setName(clientDTO.getName());
        client.setEmail(clientDTO.getEmail());
        client.setPhone(clientDTO.getPhone());
        client.setBirthday(clientDTO.getBirthday());
        return client;
    }
}