package br.org.unicortes.barbearia.mappers;

import br.org.unicortes.barbearia.dtos.ServicoDTO;
import br.org.unicortes.barbearia.models.Servico;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IServicoMapper {
    Servico toEntity(ServicoDTO servicoDTO);
    ServicoDTO toDTO(Servico servico);
}
