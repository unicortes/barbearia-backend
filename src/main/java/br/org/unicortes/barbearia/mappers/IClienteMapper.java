package br.org.unicortes.barbearia.mappers;

import br.org.unicortes.barbearia.dtos.ClienteDTO;
import br.org.unicortes.barbearia.models.Cliente;
import br.org.unicortes.barbearia.models.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IClienteMapper {

    @Mapping(target = "usuario", source = "usuarioId")
    Cliente toEntity(ClienteDTO dto);

    @Mapping(target = "usuarioId", source = "usuario.id")
    ClienteDTO toDTO(Cliente entity);

    default Usuario mapUsuario(Long id) {
        return id == null ? null : Usuario.builder().id(id).build();
    }
}
