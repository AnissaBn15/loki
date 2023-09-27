package com.loki.service.mapper;

import com.loki.domain.Command;
import com.loki.domain.Fournisseur;
import com.loki.domain.Paiement;
import com.loki.service.dto.CommandDTO;
import com.loki.service.dto.FournisseurDTO;
import com.loki.service.dto.PaiementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paiement} and its DTO {@link PaiementDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaiementMapper extends EntityMapper<PaiementDTO, Paiement> {
    @Mapping(target = "command", source = "command", qualifiedByName = "commandId")
    @Mapping(target = "fournisseur", source = "fournisseur", qualifiedByName = "fournisseurId")
    PaiementDTO toDto(Paiement s);

    @Named("commandId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommandDTO toDtoCommandId(Command command);

    @Named("fournisseurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FournisseurDTO toDtoFournisseurId(Fournisseur fournisseur);
}
