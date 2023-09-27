package com.loki.service.mapper;

import com.loki.domain.Client;
import com.loki.domain.Panier;
import com.loki.service.dto.ClientDTO;
import com.loki.service.dto.PanierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Panier} and its DTO {@link PanierDTO}.
 */
@Mapper(componentModel = "spring")
public interface PanierMapper extends EntityMapper<PanierDTO, Panier> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    PanierDTO toDto(Panier s);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);
}
