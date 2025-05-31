package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.controllers.api.ServicoApi;
import br.org.unicortes.barbearia.dtos.ServicoDTO;
import br.org.unicortes.barbearia.mappers.IServicoMapper;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import br.org.unicortes.barbearia.services.interfaces.IServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ServicoController implements ServicoApi {

    private final IServicoService servicoService;
    private final IServicoMapper servicoMapper;

    @Override
    public ResponseEntity<AbstractResponse<List<ServicoDTO>>> listarTodos() {
        List<ServicoDTO> servicos = servicoService.listarTodos()
                .stream()
                .map(servicoMapper::toDTO)
                .toList();

        return ResponseEntity.ok(AbstractResponse.success(servicos));
    }

    @Override
    public ResponseEntity<AbstractResponse<ServicoDTO>> buscarPorId(Long id) {
        Servico servico = servicoService.buscarPorId(id);
        return ResponseEntity.ok(AbstractResponse.success(servicoMapper.toDTO(servico)));
    }

    @Override
    public ResponseEntity<AbstractResponse<ServicoDTO>> criar(ServicoDTO servicoDTO) {
        Servico novoServico = servicoMapper.toEntity(servicoDTO);
        Servico servicoSalvo = servicoService.criar(novoServico);
        ServicoDTO dtoCriado = servicoMapper.toDTO(servicoSalvo);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(servicoSalvo.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(AbstractResponse.success(dtoCriado, "Serviço criado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<ServicoDTO>> atualizar(Long id, ServicoDTO servicoDTO) {
        Servico servicoAtualizado = servicoService.atualizar(id, servicoMapper.toEntity(servicoDTO));
        ServicoDTO dtoAtualizado = servicoMapper.toDTO(servicoAtualizado);
        return ResponseEntity.ok(AbstractResponse.success(dtoAtualizado, "Serviço atualizado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> remover(Long id) {
        servicoService.remover(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Serviço removido com sucesso"));
    }
}
