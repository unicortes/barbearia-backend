package br.org.unicortes.barbearia.mappers;

import br.org.unicortes.barbearia.dtos.PromocaoDTO;
import br.org.unicortes.barbearia.models.Promocao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IPromocaoMapper {
    Promocao toEntity(PromocaoDTO promocaoDTO);
    PromocaoDTO toDTO(Promocao promocao);
}
