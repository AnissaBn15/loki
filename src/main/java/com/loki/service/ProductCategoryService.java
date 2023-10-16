package com.loki.service;

import com.loki.domain.Product;
import com.loki.domain.ProductCategory;
import com.loki.repository.ProductCategoryRepository;
import com.loki.repository.ProductRepository;
import com.loki.security.SecurityUtils;
import com.loki.service.dto.ProductCategoryDTO;
import com.loki.service.dto.ProductDTO;
import com.loki.service.mapper.ProductCategoryMapper;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.loki.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductCategory}.
 */
@Service
@Transactional
public class ProductCategoryService {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryService.class);

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryMapper productCategoryMapper;

    private final ProductService productService;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository, ProductCategoryMapper productCategoryMapper, ProductService productService, ProductRepository productRepository, ProductMapper productMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
        this.productService = productService;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Save a productCategory.
     *
     * @param productCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductCategoryDTO save(ProductCategoryDTO productCategoryDTO) {
        log.debug("Request to save ProductCategory : {}", productCategoryDTO);
        ProductCategory productCategory = productCategoryMapper.toEntity(productCategoryDTO);
        productCategory = productCategoryRepository.save(productCategory);
        return productCategoryMapper.toDto(productCategory);
    }
    public ProductCategoryDTO create(ProductCategoryDTO productCategoryDTO) {
        log.debug("Request to save ProductCategory : {}", productCategoryDTO);
        ProductCategoryDTO newProductCategoryDTO = new ProductCategoryDTO();
        newProductCategoryDTO.setName(productCategoryDTO.getName());
        newProductCategoryDTO.setCreated(ZonedDateTime.now());
        newProductCategoryDTO.setCreatedBy(SecurityUtils.getCurrentUserLogin().orElse(null));
        ProductCategory savedProductCategory = productCategoryMapper.toEntity(productCategoryDTO);
        savedProductCategory = productCategoryRepository.save(savedProductCategory);
        List<ProductDTO> productDTOList = productCategoryDTO.getProducts().stream().collect(Collectors.toList());
        for (ProductDTO productDTO : productDTOList) {
            productDTO.setProductCategoryId(savedProductCategory.getId());
            productDTO.setReference(productDTO.getReference());
            productDTO.setName(productDTO.getName());
            productDTO.setDescription(productDTO.getDescription());
            productDTO.setProductStatus(productDTO.getProductStatus());
            productDTO.setActive(productDTO.getActive());
            productDTO.setQuantityInStock(productDTO.getQuantityInStock());
            productDTO.setNbrOfSells(productDTO.getNbrOfSells());
            productDTO.setImagePath(productDTO.getImagePath());
            productDTO.setMinimalQuantity(productDTO.getMinimalQuantity());
            productDTO.setMaximalQuantity(productDTO.getMaximalQuantity());
            productDTO.setWeightedAveragePrice(productDTO.getWeightedAveragePrice());
            productDTO.setLocation(productDTO.getLocation());
            productDTO.setConsumptionDeadline(productDTO.getConsumptionDeadline());
            productDTO.setBarCode(productDTO.getBarCode());
            productDTO.setSerialNumber(productDTO.getSerialNumber());
            productDTO.setBrand(productDTO.getBrand());
            productDTO.setModel(productDTO.getModel());
            productDTO.setSection(productDTO.getSection());
            productDTO.setHallway(productDTO.getHallway());
            productDTO.setProductDisplay(productDTO.getProductDisplay());
            productDTO.setLocker(productDTO.getLocker());
            productDTO.setProductCode(productDTO.getProductCode());
            productDTO.setCreated(ZonedDateTime.now());
            productDTO.setCreatedBy(SecurityUtils.getCurrentUserLogin().orElse(null));
            productDTO.setUpdated(ZonedDateTime.now());
        }
        productService.saveAll(productDTOList);
        productCategoryDTO.setId(savedProductCategory.getId());
        return productCategoryDTO;
    }
    /**
     * Update a productCategory.
     *
     * @param productCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductCategoryDTO update(ProductCategoryDTO productCategoryDTO) {
        log.debug("Request to update ProductCategory : {}", productCategoryDTO);
        ProductCategory productCategory = productCategoryMapper.toEntity(productCategoryDTO);
        productCategory = productCategoryRepository.save(productCategory);
        return productCategoryMapper.toDto(productCategory);
    }

    /**
     * Partially update a productCategory.
     *
     * @param productCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductCategoryDTO> partialUpdate(ProductCategoryDTO productCategoryDTO) {
        log.debug("Request to partially update ProductCategory : {}", productCategoryDTO);

        return productCategoryRepository
            .findById(productCategoryDTO.getId())
            .map(existingProductCategory -> {
                productCategoryMapper.partialUpdate(existingProductCategory, productCategoryDTO);

                return existingProductCategory;
            })
            .map(productCategoryRepository::save)
            .map(productCategoryMapper::toDto);
    }

    /**
     * Get all the productCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductCategories");
        return productCategoryRepository.findAll(pageable).map(productCategoryMapper::toDto);
    }

    /**
     * Get one productCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductCategoryDTO> findOne(Long id) {
        log.debug("Request to get ProductCategory : {}", id);
        return productCategoryRepository.findById(id).map(productCategoryMapper::toDto);
    }

    /**
     * Delete the productCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductCategory : {}", id);
        productCategoryRepository.deleteById(id);
    }
}
