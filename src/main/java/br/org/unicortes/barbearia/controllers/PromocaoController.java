package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.controllers.api.PromocaoApi;
import br.org.unicortes.barbearia.dtos.PromocaoDTO;
import br.org.unicortes.barbearia.mappers.IPromocaoMapper;
import br.org.unicortes.barbearia.models.Promocao;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import br.org.unicortes.barbearia.services.interfaces.IPromocaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PromocaoController implements PromocaoApi {

    private final IPromocaoService promocaoService;
    private final IPromocaoMapper IPromocaoMapper;

    @Override
    public ResponseEntity<AbstractResponse<List<PromocaoDTO>>> listarTodos() {
        List<PromocaoDTO> promocoes = promocaoService.listarTodos()
                .stream()
                .map(IPromocaoMapper::toDTO)
                .toList();

        return ResponseEntity.ok(AbstractResponse.success(promocoes));
    }

    @Override
    public ResponseEntity<AbstractResponse<PromocaoDTO>> buscarPorId(Long id) {
        Promocao promocao = promocaoService.buscarPorId(id);
        return ResponseEntity.ok(AbstractResponse.success(IPromocaoMapper.toDTO(promocao)));
    }

    @Override
    public ResponseEntity<AbstractResponse<PromocaoDTO>> criar(PromocaoDTO promocaoDTO) {
        Promocao novaPromocao = IPromocaoMapper.toEntity(promocaoDTO);
        Promocao promocaoSalva = promocaoService.criar(novaPromocao);
        PromocaoDTO dtoCriado = IPromocaoMapper.toDTO(promocaoSalva);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(promocaoSalva.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(AbstractResponse.success(dtoCriado, "Promoção criada com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<PromocaoDTO>> atualizar(Long id, PromocaoDTO promocaoDTO) {
        Promocao promocaoAtualizada = promocaoService.atualizar(id, IPromocaoMapper.toEntity(promocaoDTO));
        PromocaoDTO dtoAtualizado = IPromocaoMapper.toDTO(promocaoAtualizada);
        return ResponseEntity.ok(AbstractResponse.success(dtoAtualizado, "Promoção atualizada com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> remover(Long id) {
        promocaoService.remover(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Promoção removida com sucesso"));
    }
}
