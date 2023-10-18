package com.loki.service.mapper;
import com.loki.domain.Product;
import com.loki.domain.ProductCategory;
import com.loki.service.dto.ProductCategoryDTO;
import com.loki.service.dto.ProductDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */

@Mapper(componentModel = "spring" ,uses = {ProductCategoryMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(source = "product.id", target = "productCategoryId")
    ProductDTO toDto(Product product);

    @Mapping(source = "productCategoryId", target = "productCategory")
    Product toEntity(ProductDTO productDTO);

    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
    }
