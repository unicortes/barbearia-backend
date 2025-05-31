package br.org.unicortes.barbearia.mappers;

import br.org.unicortes.barbearia.dtos.ProdutoDTO;
import br.org.unicortes.barbearia.models.Produto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IProdutoMapper {
    Produto toEntity(ProdutoDTO produtoDTO);
    ProdutoDTO toDTO(Produto produto);
}
