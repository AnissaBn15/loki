package com.loki.service.mapper;

import com.loki.domain.ProductCategory;
import com.loki.service.dto.ProductCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductCategory} and its DTO {@link ProductCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductCategoryMapper extends EntityMapper<ProductCategoryDTO, ProductCategory> {
    @Mapping(target = "products", source = "products")
    ProductCategoryDTO toDto(ProductCategory productCategory);

    @Mapping(target = "products", source = "products")
    ProductCategory toEntity(ProductCategoryDTO productCategoryDTO);

    default ProductCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(id);
        return productCategory;
    }

}
