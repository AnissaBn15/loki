package com.loki.service.mapper;

import com.loki.domain.Fournisseur;
import com.loki.service.dto.FournisseurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fournisseur} and its DTO {@link FournisseurDTO}.
 */
@Mapper(componentModel = "spring")
public interface FournisseurMapper extends EntityMapper<FournisseurDTO, Fournisseur> {}
