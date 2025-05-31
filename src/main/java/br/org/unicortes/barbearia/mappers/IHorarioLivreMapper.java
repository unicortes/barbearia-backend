package br.org.unicortes.barbearia.mappers;

import br.org.unicortes.barbearia.dtos.HorarioLivreDTO;
import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.models.HorarioLivre;
import br.org.unicortes.barbearia.models.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IHorarioLivreMapper {

    @Mapping(target = "barbeiro", source = "barbeiroId")
    @Mapping(target = "servico", source = "servicoId")
    @Mapping(target = "version", ignore = true)
    HorarioLivre toEntity(HorarioLivreDTO dto);

    @Mapping(target = "barbeiroId", source = "barbeiro.id")
    @Mapping(target = "servicoId", source = "servico.id")
    HorarioLivreDTO toDTO(HorarioLivre entity);

    default Barbeiro mapBarbeiro(Long id) {
        return id == null ? null : Barbeiro.builder().id(id).build();
    }

    default Servico map(Long id) {
        return id == null ? null : Servico.builder().id(id).build();
    }
}
