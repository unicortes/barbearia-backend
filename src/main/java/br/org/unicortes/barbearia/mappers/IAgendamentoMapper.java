package br.org.unicortes.barbearia.mappers;

import br.org.unicortes.barbearia.dtos.AgendamentoDTO;
import br.org.unicortes.barbearia.models.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IAgendamentoMapper {

    @Mapping(target = "servico", source = "servicoId")
    @Mapping(target = "barbeiro", source = "barbeiroId")
    @Mapping(target = "cliente", source = "clienteId")
    @Mapping(target = "horarioLivre", source = "horarioLivreId")
    Agendamento toEntity(AgendamentoDTO dto);

    @Mapping(target = "servicoId", source = "servico.id")
    @Mapping(target = "barbeiroId", source = "barbeiro.id")
    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "horarioLivreId", source = "horarioLivre.id")
    AgendamentoDTO toDTO(Agendamento entity);

    default Servico map(Long id) {
        return id == null ? null : Servico.builder().id(id).build();
    }

    default Barbeiro mapBarbeiro(Long id) {
        return id == null ? null : Barbeiro.builder().id(id).build();
    }

    default Cliente mapCliente(Long id) {
        return id == null ? null : Cliente.builder().id(id).build();
    }

    default HorarioLivre mapHorarioLivre(Long id) {
        return id == null ? null : HorarioLivre.builder().id(id).build();
    }
}
