package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Cliente;
import com.mycompany.myapp.domain.ResponsavelLegal;
import com.mycompany.myapp.domain.Tag;
import com.mycompany.myapp.service.dto.ClienteDTO;
import com.mycompany.myapp.service.dto.ResponsavelLegalDTO;
import com.mycompany.myapp.service.dto.TagDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {
    @Mapping(target = "responsavelLegal", source = "responsavelLegal", qualifiedByName = "responsavelLegalNome")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagNomeSet")
    ClienteDTO toDto(Cliente s);

    @Mapping(target = "removeTags", ignore = true)
    Cliente toEntity(ClienteDTO clienteDTO);

    @Named("responsavelLegalNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    ResponsavelLegalDTO toDtoResponsavelLegalNome(ResponsavelLegal responsavelLegal);

    @Named("tagNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    TagDTO toDtoTagNome(Tag tag);

    @Named("tagNomeSet")
    default Set<TagDTO> toDtoTagNomeSet(Set<Tag> tag) {
        return tag.stream().map(this::toDtoTagNome).collect(Collectors.toSet());
    }
}
