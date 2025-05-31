package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.controllers.api.EstoqueApi;
import br.org.unicortes.barbearia.dtos.EstoqueDTO;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import br.org.unicortes.barbearia.mappers.IEstoqueMapper;
import br.org.unicortes.barbearia.models.Estoque;
import br.org.unicortes.barbearia.services.interfaces.IEstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks")
@CrossOrigin("*")
public class EstoqueController implements EstoqueApi {

    private final IEstoqueService estoqueService;
    private final IEstoqueMapper estoqueMapper;

    @GetMapping
    public ResponseEntity<AbstractResponse<List<EstoqueDTO>>> listarTodos() {
        List<EstoqueDTO> lista = estoqueService.listarTodos()
                .stream()
                .map(estoqueMapper::toDTO)
                .toList();
        return ResponseEntity.ok(AbstractResponse.success(lista));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbstractResponse<EstoqueDTO>> buscarPorId(@PathVariable Long id) {
        Estoque estoque = estoqueService.buscarPorId(id);
        EstoqueDTO dto = estoqueMapper.toDTO(estoque);
        return ResponseEntity.ok(AbstractResponse.success(dto));
    }

    @PostMapping
    public ResponseEntity<AbstractResponse<EstoqueDTO>> criar(@RequestBody EstoqueDTO dto) {
        Estoque entidade = estoqueMapper.toEntity(dto);
        Estoque criado = estoqueService.criar(entidade);
        EstoqueDTO dtoCriado = estoqueMapper.toDTO(criado);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(AbstractResponse.success(dtoCriado, "Estoque criado com sucesso"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AbstractResponse<EstoqueDTO>> atualizar(@PathVariable Long id, @RequestBody EstoqueDTO dto) {
        Estoque entidade = estoqueMapper.toEntity(dto);
        Estoque atualizado = estoqueService.atualizar(id, entidade);
        EstoqueDTO dtoAtualizado = estoqueMapper.toDTO(atualizado);
        return ResponseEntity.ok(AbstractResponse.success(dtoAtualizado, "Estoque atualizado com sucesso"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AbstractResponse<Void>> remover(@PathVariable Long id) {
        estoqueService.remover(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Estoque removido com sucesso"));
    }

}
