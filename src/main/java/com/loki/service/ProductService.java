package com.loki.service;

import com.loki.domain.Product;
//import com.loki.domain.Product_;
import com.loki.repository.ProductRepository;
import com.loki.service.dto.ProductDTO;
import com.loki.service.mapper.ProductMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Save a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    /**
     * Update a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductDTO update(ProductDTO productDTO) {
        log.debug("Request to update Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    /**
     * Partially update a product.
     *
     * @param productDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductDTO> partialUpdate(ProductDTO productDTO) {
        log.debug("Request to partially update Product : {}", productDTO);

        return productRepository
            .findById(productDTO.getId())
            .map(existingProduct -> {
                productMapper.partialUpdate(existingProduct, productDTO);

                return existingProduct;
            })
            .map(productRepository::save)
            .map(productMapper::toDto);
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        Page<Product> products = productRepository.findAll(pageable);
        log.debug("Request to get all Products :" + products);
        // Force l'initialisation explicite des associations nécessaires
        products.forEach(product -> Hibernate.initialize(product.getProductCategory()));
        return products.map(productMapper::toDto);
    }

    /*@Transactional(readOnly = true)
    public Page<ProductDTO> getProductsByProductCategoryIdUsingCustomQuery(Long categoryId, Pageable pageable) {
        Specification<Product> specification = Specification.where(getProductsByCategory(categoryId));
        Page<Product> productList = productRepository.findAll(specification, pageable);
        log.debug("Request to get all Products By Category Id :" + productList);
        return productList.map(productMapper::toDto);
    }*/

    /*private Specification<Product> getProductsByCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Product_.productCategory), categoryId);
    }*/

    @Transactional(readOnly = true)
    public Page<ProductDTO> getBestSellingProducts(Pageable pageable) {
        Specification<Product> specification = Specification.where(getBestSellingProductsSpecification());
        Page<Product> productList = productRepository.findAll(specification, pageable);
        log.debug("Request to get Best Selling Products: " + productList);
        return productList.map(productMapper::toDto);
    }

    private Specification<Product> getBestSellingProductsSpecification() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("nbrOfSells")));
            return criteriaBuilder.isNotNull(root.get("nbrOfSells"));
        };
    }

    public void saveAll(List<ProductDTO> productDTOList) {
        productRepository.saveAll(productMapper.toEntity(productDTOList));
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id).map(productMapper::toDto);
    }


    /**
     * Delete the product by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }
}
