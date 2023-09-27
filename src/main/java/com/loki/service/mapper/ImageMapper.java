package com.loki.service.mapper;

import com.loki.domain.Image;
import com.loki.domain.Product;
import com.loki.service.dto.ImageDTO;
import com.loki.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Image} and its DTO {@link ImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImageMapper extends EntityMapper<ImageDTO, Image> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    ImageDTO toDto(Image s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
