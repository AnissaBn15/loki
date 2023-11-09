package com.loki.service.mapper;

import com.loki.domain.Client;
import com.loki.domain.Command;
import com.loki.domain.LineOfCommand;
import com.loki.domain.Panier;
import com.loki.domain.Product;
import com.loki.service.dto.ClientDTO;
import com.loki.service.dto.CommandDTO;
import com.loki.service.dto.LineOfCommandDTO;
import com.loki.service.dto.PanierDTO;
import com.loki.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LineOfCommand} and its DTO {@link LineOfCommandDTO}.
 */
@Mapper(componentModel = "spring")
public interface LineOfCommandMapper extends EntityMapper<LineOfCommandDTO, LineOfCommand> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "panier", source = "panier", qualifiedByName = "panierId")
    @Mapping(target = "command", source = "command", qualifiedByName = "commandId")
    LineOfCommandDTO toDto(LineOfCommand s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("panierId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PanierDTO toDtoPanierId(Panier panier);

    @Named("commandId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommandDTO toDtoCommandId(Command command);
}
