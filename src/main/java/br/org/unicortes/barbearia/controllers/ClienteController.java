package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.controllers.api.ClienteApi;
import br.org.unicortes.barbearia.dtos.ClienteDTO;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import br.org.unicortes.barbearia.mappers.IClienteMapper;
import br.org.unicortes.barbearia.models.Cliente;
import br.org.unicortes.barbearia.services.interfaces.IClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClienteController implements ClienteApi {

    private final IClienteService clienteService;
    private final IClienteMapper clienteMapper;

    @Override
    public ResponseEntity<AbstractResponse<List<ClienteDTO>>> listarTodos() {
        List<ClienteDTO> clientes = clienteService.listarTodos()
                .stream()
                .map(clienteMapper::toDTO)
                .toList();
        return ResponseEntity.ok(AbstractResponse.success(clientes));
    }

    @Override
    public ResponseEntity<AbstractResponse<ClienteDTO>> buscarPorId(Long id) {
        ClienteDTO cliente = clienteMapper.toDTO(clienteService.buscarPorId(id));
        return ResponseEntity.ok(AbstractResponse.success(cliente));
    }

    @Override
    public ResponseEntity<AbstractResponse<ClienteDTO>> criar(ClienteDTO clienteDTO) {
        Cliente cliente = clienteService.criar(clienteMapper.toEntity(clienteDTO));
        ClienteDTO clienteCriado = clienteMapper.toDTO(cliente);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(AbstractResponse.success(clienteCriado, "Cliente criado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<ClienteDTO>> atualizar(Long id, ClienteDTO clienteDTO) {
        Cliente clienteAtualizado = clienteService.atualizar(id, clienteMapper.toEntity(clienteDTO));
        ClienteDTO dtoAtualizado = clienteMapper.toDTO(clienteAtualizado);
        return ResponseEntity.ok(AbstractResponse.success(dtoAtualizado, "Cliente atualizado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> remover(Long id) {
        clienteService.remover(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Cliente removido com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> ativar(Long id) {
        clienteService.ativarCliente(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Cliente ativado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> desativar(Long id) {
        clienteService.desativarCliente(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Cliente desativado com sucesso"));
    }
}
