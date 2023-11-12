package com.loki.service;

import com.loki.domain.Client;
import com.loki.domain.LineOfCommand;
import com.loki.domain.Product;
import com.loki.repository.ClientRepository;
import com.loki.repository.LineOfCommandRepository;
import com.loki.repository.ProductRepository;
import com.loki.service.dto.LineOfCommandDTO;
import com.loki.service.dto.ProductCategoryDTO;
import com.loki.service.dto.ProductDTO;
import com.loki.service.mapper.LineOfCommandMapper;
import java.util.Optional;

import com.loki.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * Service Implementation for managing {@link LineOfCommand}.
 */
@Service
@Transactional
public class LineOfCommandService {

    private final Logger log = LoggerFactory.getLogger(LineOfCommandService.class);

    private final LineOfCommandRepository lineOfCommandRepository;

    private final LineOfCommandMapper lineOfCommandMapper;


    private final ClientRepository clientRepository;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;


    public LineOfCommandService(LineOfCommandRepository lineOfCommandRepository, LineOfCommandMapper lineOfCommandMapper, ClientRepository clientRepository, ProductRepository productRepository, ProductMapper productMapper) {
        this.lineOfCommandRepository = lineOfCommandRepository;
        this.lineOfCommandMapper = lineOfCommandMapper;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Save a lineOfCommand.
     *
     * @param lineOfCommandDTO the entity to save.
     * @return the persisted entity.
     */

    @Transactional
    public LineOfCommandDTO save(LineOfCommandDTO lineOfCommandDTO) {
        log.debug("Request to save LineOfCommand : {}", lineOfCommandDTO);
        LineOfCommand lineOfCommand = lineOfCommandMapper.toEntity(lineOfCommandDTO);
        Long clientId = lineOfCommandDTO.getClientId();
        Long productId = lineOfCommandDTO.getProductId();
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        lineOfCommand.setClient(client);
        lineOfCommand.setProduct(product);
        lineOfCommand = lineOfCommandRepository.save(lineOfCommand);
        //update product
        Product productUpdate = productRepository.findById(productId).get();
        productUpdate.setBarCode(productUpdate.getBarCode());
        productUpdate.setQuantityInStock(productUpdate.getQuantityInStock() - lineOfCommandDTO.getQuantity());
        productUpdate.setNbrOfSells(productUpdate.getNbrOfSells() + lineOfCommand.getQuantity());
        productRepository.save(productUpdate);
        return lineOfCommandMapper.toDto(lineOfCommand);
    }

    /**
     * Update a lineOfCommand.
     *
     * @param lineOfCommandDTO the entity to save.
     * @return the persisted entity.
     */
    public LineOfCommandDTO update(LineOfCommandDTO lineOfCommandDTO) {
        log.debug("Request to update LineOfCommand : {}", lineOfCommandDTO);
        LineOfCommand lineOfCommand = lineOfCommandMapper.toEntity(lineOfCommandDTO);
        lineOfCommand = lineOfCommandRepository.save(lineOfCommand);
        return lineOfCommandMapper.toDto(lineOfCommand);
    }

    /**
     * Partially update a lineOfCommand.
     *
     * @param lineOfCommandDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LineOfCommandDTO> partialUpdate(LineOfCommandDTO lineOfCommandDTO) {
        log.debug("Request to partially update LineOfCommand : {}", lineOfCommandDTO);

        return lineOfCommandRepository
            .findById(lineOfCommandDTO.getId())
            .map(existingLineOfCommand -> {
                lineOfCommandMapper.partialUpdate(existingLineOfCommand, lineOfCommandDTO);

                return existingLineOfCommand;
            })
            .map(lineOfCommandRepository::save)
            .map(lineOfCommandMapper::toDto);
    }

    /**
     * Get all the lineOfCommands.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LineOfCommandDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LineOfCommands");
        return lineOfCommandRepository.findAll(pageable).map(lineOfCommandMapper::toDto);
    }

    /**
     * Get one lineOfCommand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LineOfCommandDTO> findOne(Long id) {
        log.debug("Request to get LineOfCommand : {}", id);
        return lineOfCommandRepository.findById(id).map(lineOfCommandMapper::toDto);
    }

    /**
     * Delete the lineOfCommand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LineOfCommand : {}", id);
        lineOfCommandRepository.deleteById(id);
    }
}
