package br.org.unicortes.barbearia.mappers;

import br.org.unicortes.barbearia.dtos.UsuarioDTO;
import br.org.unicortes.barbearia.models.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUsuarioMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    Usuario toEntity(UsuarioDTO dto);

    UsuarioDTO toDTO(Usuario entity);
}