package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.controllers.api.BarbeiroApi;
import br.org.unicortes.barbearia.dtos.BarbeiroDTO;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import br.org.unicortes.barbearia.mappers.IBarbeiroMapper;
import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.services.interfaces.IBarbeiroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BarbeiroController implements BarbeiroApi {

    private final IBarbeiroService barbeiroService;
    private final IBarbeiroMapper barbeiroMapper;

    @Override
    public ResponseEntity<AbstractResponse<List<BarbeiroDTO>>> listarTodos() {
        List<BarbeiroDTO> barbeiros = barbeiroService.listarTodos()
                .stream()
                .map(barbeiroMapper::toDTO)
                .toList();
        return ResponseEntity.ok(AbstractResponse.success(barbeiros));
    }

    @Override
    public ResponseEntity<AbstractResponse<BarbeiroDTO>> buscarPorId(Long id) {
        BarbeiroDTO barbeiro = barbeiroMapper.toDTO(barbeiroService.buscarPorId(id));
        return ResponseEntity.ok(AbstractResponse.success(barbeiro));
    }

    @Override
    public ResponseEntity<AbstractResponse<BarbeiroDTO>> criar(BarbeiroDTO barbeiroDTO) {
        Barbeiro barbeiro = barbeiroService.criar(barbeiroMapper.toEntity(barbeiroDTO));
        BarbeiroDTO criado = barbeiroMapper.toDTO(barbeiro);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(barbeiro.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(AbstractResponse.success(criado, "Barbeiro criado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<BarbeiroDTO>> atualizar(Long id, BarbeiroDTO barbeiroDTO) {
        Barbeiro atualizado = barbeiroService.atualizar(id, barbeiroMapper.toEntity(barbeiroDTO));
        BarbeiroDTO dtoAtualizado = barbeiroMapper.toDTO(atualizado);
        return ResponseEntity.ok(AbstractResponse.success(dtoAtualizado, "Barbeiro atualizado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> remover(Long id) {
        barbeiroService.remover(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Barbeiro removido com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> ativar(Long id) {
        barbeiroService.ativarBarbeiro(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Barbeiro ativado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> desativar(Long id) {
        barbeiroService.desativarBarbeiro(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Barbeiro desativado com sucesso"));
    }
}
