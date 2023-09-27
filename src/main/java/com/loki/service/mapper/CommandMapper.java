package com.loki.service.mapper;

import com.loki.domain.Client;
import com.loki.domain.Command;
import com.loki.domain.Fournisseur;
import com.loki.service.dto.ClientDTO;
import com.loki.service.dto.CommandDTO;
import com.loki.service.dto.FournisseurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Command} and its DTO {@link CommandDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommandMapper extends EntityMapper<CommandDTO, Command> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "fournisseur", source = "fournisseur", qualifiedByName = "fournisseurId")
    CommandDTO toDto(Command s);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("fournisseurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FournisseurDTO toDtoFournisseurId(Fournisseur fournisseur);
}
