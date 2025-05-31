package br.org.unicortes.barbearia.mappers;

import br.org.unicortes.barbearia.dtos.EstoqueDTO;
import br.org.unicortes.barbearia.models.Estoque;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IEstoqueMapper {

    @Mapping(target = "produto.id", source = "produto")
    Estoque toEntity(EstoqueDTO estoqueDTO);

    @Mapping(target = "produto", source = "produto.id")
    EstoqueDTO toDTO(Estoque estoque);
}
