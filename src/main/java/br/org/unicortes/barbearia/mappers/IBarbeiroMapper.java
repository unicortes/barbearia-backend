package br.org.unicortes.barbearia.mappers;

import br.org.unicortes.barbearia.dtos.BarbeiroDTO;
import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.models.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { IHorarioLivreMapper.class, IAgendamentoMapper.class })
public interface IBarbeiroMapper {

    @Mapping(target = "usuario", source = "usuarioId")
    Barbeiro toEntity(BarbeiroDTO dto);

    @Mapping(target = "usuarioId", source = "usuario.id")
    BarbeiroDTO toDTO(Barbeiro entity);

    default Usuario mapUsuario(Long id) {
        return id == null ? null : Usuario.builder().id(id).build();
    }
}
