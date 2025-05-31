package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.controllers.api.HorarioLivreApi;
import br.org.unicortes.barbearia.dtos.HorarioLivreDTO;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import br.org.unicortes.barbearia.mappers.IHorarioLivreMapper;
import br.org.unicortes.barbearia.models.HorarioLivre;
import br.org.unicortes.barbearia.services.interfaces.IHorarioLivreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HorarioLivreController implements HorarioLivreApi {

    private final IHorarioLivreService horarioLivreService;
    private final IHorarioLivreMapper horarioLivreMapper;

    @Override
    public ResponseEntity<AbstractResponse<List<HorarioLivreDTO>>> listarTodos() {
        List<HorarioLivreDTO> horarios = horarioLivreService.listarTodos()
                .stream()
                .map(horarioLivreMapper::toDTO)
                .toList();
        return ResponseEntity.ok(AbstractResponse.success(horarios));
    }

    @Override
    public ResponseEntity<AbstractResponse<HorarioLivreDTO>> buscarPorId(Long id) {
        HorarioLivre horario = horarioLivreService.buscarPorId(id);
        return ResponseEntity.ok(AbstractResponse.success(horarioLivreMapper.toDTO(horario)));
    }

    @Override
    public ResponseEntity<AbstractResponse<HorarioLivreDTO>> criar(HorarioLivreDTO dto) {
        HorarioLivre novo = horarioLivreService.criar(horarioLivreMapper.toEntity(dto));
        HorarioLivreDTO dtoCriado = horarioLivreMapper.toDTO(novo);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novo.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(AbstractResponse.success(dtoCriado, "Horário criado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<HorarioLivreDTO>> atualizar(Long id, HorarioLivreDTO dto) {
        HorarioLivre atualizado = horarioLivreService.atualizar(id, horarioLivreMapper.toEntity(dto));
        HorarioLivreDTO dtoAtualizado = horarioLivreMapper.toDTO(atualizado);
        return ResponseEntity.ok(AbstractResponse.success(dtoAtualizado, "Horário atualizado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> remover(Long id) {
        horarioLivreService.remover(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Horário removido com sucesso"));
    }
}
