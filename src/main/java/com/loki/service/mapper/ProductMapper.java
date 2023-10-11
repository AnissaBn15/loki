package com.loki.service.mapper;

import com.loki.domain.Fournisseur;
import com.loki.domain.Product;
import com.loki.domain.ProductCategory;
import com.loki.service.dto.FournisseurDTO;
import com.loki.service.dto.ProductCategoryDTO;
import com.loki.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "fournisseur", source = "fournisseur", qualifiedByName = "fournisseurId")
    @Mapping(target = "productCategory", source = "productCategory", qualifiedByName = "productCategoryId")
    @Mapping(target = "productCategoryId", source = "productCategory.id")
    ProductDTO toDto(Product s);

    @Named("fournisseurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FournisseurDTO toDtoFournisseurId(Fournisseur fournisseur);

    @Named("productCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductCategoryDTO toDtoProductCategoryId(ProductCategory productCategory);
}
