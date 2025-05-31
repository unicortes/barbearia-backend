package br.org.unicortes.barbearia.mappers;

import br.org.unicortes.barbearia.dtos.CartaoFidelidadeDTO;
import br.org.unicortes.barbearia.models.CartaoFidelidade;
import br.org.unicortes.barbearia.models.Cliente;
import br.org.unicortes.barbearia.models.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ICartaoFidelidadeMapper {

    @Mapping(target = "cliente", source = "clienteId")
    @Mapping(target = "servico", source = "servicoId")
    CartaoFidelidade toEntity(CartaoFidelidadeDTO dto);

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "servicoId", source = "servico.id")
    CartaoFidelidadeDTO toDTO(CartaoFidelidade entity);

    default Cliente mapCliente(Long id) {
        return id == null ? null : Cliente.builder().id(id).build();
    }

    default Servico mapServico(Long id) {
        return id == null ? null : Servico.builder().id(id).build();
    }
}
