package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.controllers.api.ProdutoApi;
import br.org.unicortes.barbearia.dtos.ProdutoDTO;
import br.org.unicortes.barbearia.responses.AbstractResponse;
import br.org.unicortes.barbearia.mappers.IProdutoMapper;
import br.org.unicortes.barbearia.models.Produto;
import br.org.unicortes.barbearia.services.interfaces.IProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProdutoController implements ProdutoApi {

    private final IProdutoService produtoService;
    private final IProdutoMapper IProdutoMapper;

    @Override
    public ResponseEntity<AbstractResponse<List<ProdutoDTO>>> listarTodos() {
        List<ProdutoDTO> produtos = produtoService.listarTodos()
                .stream()
                .map(IProdutoMapper::toDTO)
                .toList();

        return ResponseEntity.ok(AbstractResponse.success(produtos));
    }

    @Override
    public ResponseEntity<AbstractResponse<ProdutoDTO>> buscarPorId(Long id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(AbstractResponse.success(IProdutoMapper.toDTO(produto)));
    }

    @Override
    public ResponseEntity<AbstractResponse<ProdutoDTO>> criar(ProdutoDTO produtoDTO) {
        Produto novoProduto = IProdutoMapper.toEntity(produtoDTO);
        Produto produtoSalvo = produtoService.criar(novoProduto);
        ProdutoDTO produtoCriadoDTO = IProdutoMapper.toDTO(produtoSalvo);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produtoSalvo.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(AbstractResponse.success(produtoCriadoDTO, "Produto criado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<ProdutoDTO>> atualizar(Long id, ProdutoDTO produtoDTO) {
        Produto produtoAtualizado = produtoService.atualizar(id, IProdutoMapper.toEntity(produtoDTO));
        ProdutoDTO dtoAtualizado = IProdutoMapper.toDTO(produtoAtualizado);
        return ResponseEntity.ok(AbstractResponse.success(dtoAtualizado, "Produto atualizado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> remover(Long id) {
        produtoService.remover(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Produto removido com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> ativar(Long id) {
        produtoService.ativarProduto(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Produto ativado com sucesso"));
    }

    @Override
    public ResponseEntity<AbstractResponse<Void>> desativar(Long id) {
        produtoService.desativarProduto(id);
        return ResponseEntity.ok(AbstractResponse.success(null, "Produto desativado com sucesso"));
    }
}
