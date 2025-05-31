package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.controllers.api.CartaoFidelidadeApi;
import br.org.unicortes.barbearia.dtos.CartaoFidelidadeDTO;
import br.org.unicortes.barbearia.mappers.ICartaoFidelidadeMapper;
import br.org.unicortes.barbearia.models.CartaoFidelidade;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import br.org.unicortes.barbearia.services.interfaces.ICartaoFidelidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loyalty-cards")
@CrossOrigin("*")
public class CartaoFidelidadeController implements CartaoFidelidadeApi {

    private final ICartaoFidelidadeService cartaoFidelidadeService;
    private final ICartaoFidelidadeMapper cartaoFidelidadeMapper;

    @Override
    public ResponseEntity<AbstractResponse<List<CartaoFidelidadeDTO>>> listarTodos() {
        List<CartaoFidelidadeDTO> lista = cartaoFidelidadeService.listarTodos()
                .stream()
                .map(cartaoFidelidadeMapper::toDTO)
                .toList();
        return ResponseEntity.ok(AbstractResponse.success(lista));
    }

    @Override
    public ResponseEntity<AbstractResponse<CartaoFidelidadeDTO>> buscarPorId(Long id) {
        CartaoFidelidade loyaltyCard = cartaoFidelidadeService.buscarPorId(id);
        CartaoFidelidadeDTO dto = cartaoFidelidadeMapper.toDTO(loyaltyCard);
        return ResponseEntity.ok(AbstractResponse.success(dto));
    }

    @Override
    public ResponseEntity<AbstractResponse<CartaoFidelidadeDTO>> criar(CartaoFidelidadeDTO dto) {
        CartaoFidelidade entidade = cartaoFidelidadeService.criar(cartaoFidelidadeMapper.toEntity(dto));
        CartaoFidelidadeDTO dtoCriado = cartaoFidelidadeMapper.toDTO(entidade);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dtoCriado.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(AbstractResponse.success(dtoCriado, "Cartão de fidelidade criado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<CartaoFidelidadeDTO>> atualizarPontos(Long id, Integer pontos) {
        CartaoFidelidade atualizado = cartaoFidelidadeService.atualizarPontos(id, pontos);
        CartaoFidelidadeDTO dtoAtualizado = cartaoFidelidadeMapper.toDTO(atualizado);
        return ResponseEntity.ok(AbstractResponse.success(dtoAtualizado, "Pontos atualizados com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> remover(Long id) {
        cartaoFidelidadeService.remover(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Cartão de fidelidade removido com sucesso"));
    }
}
