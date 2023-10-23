package com.loki.service;

import com.loki.domain.*;
import com.loki.domain.enumeration.PanierStatus;
import com.loki.repository.ClientRepository;
import com.loki.repository.PanierRepository;
import com.loki.repository.ProductRepository;
import com.loki.repository.UserRepository;
import com.loki.security.SecurityUtils;
import com.loki.service.dto.PanierDTO;
import com.loki.service.dto.ProductDTO;
import com.loki.service.mapper.PanierMapper;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import com.loki.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * Service Implementation for managing {@link Panier}.
 */
@Service
@Transactional
public class PanierService {

    private final Logger log = LoggerFactory.getLogger(PanierService.class);

    private final PanierRepository panierRepository;

    private final ProductRepository productRepository;

    private final PanierMapper panierMapper;

    private final ProductMapper productMapper;

    private final UserRepository userRepository;
    private final ProductService productService;


    private final ClientRepository clientRepository;

    public PanierService(PanierRepository panierRepository, ProductRepository productRepository, PanierMapper panierMapper, ProductMapper productMapper, UserRepository userRepository, ProductService productService, ClientRepository clientRepository) {
        this.panierRepository = panierRepository;
        this.productRepository = productRepository;
        this.panierMapper = panierMapper;
        this.productMapper = productMapper;
        this.userRepository = userRepository;
        this.productService = productService;
        this.clientRepository = clientRepository;
    }

    /**
     * Save a panier.
     *
     * @param panierDTO the entity to save.
     * @return the persisted entity.
     */
    public PanierDTO save(PanierDTO panierDTO) {
        log.debug("Request to save Panier : {}", panierDTO);
        Panier panier = panierMapper.toEntity(panierDTO);
        panier = panierRepository.save(panier);
        return panierMapper.toDto(panier);
    }

    public PanierDTO createPanierForClient(Long id, int quantity) {
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElse(null);
        Optional<User> userOptional = userRepository.findOneByLogin(currentLogin);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Long userId = user.getId();
            Panier panier = panierRepository.findFirstByClient_IdAndStatusOrderByCreatedDesc(userId, PanierStatus.EN_COURS);
            if (panier == null) {
                panier = new Panier();
                panier.setClient(clientRepository.findById(userId).orElse(null));
                panier.setStatus(PanierStatus.EN_COURS);
                panier.setCreated(ZonedDateTime.now());
                panierRepository.save(panier);
            }
            Optional<ProductDTO> productDTOOptional = productService.findOne(id);
            if (productDTOOptional.isPresent()) {
                ProductDTO productDTO = productDTOOptional.get();
                Product product = productMapper.toEntity(productDTO);
                if (product.getQuantityInStock() >= quantity) {
                    LineOfCommand lineOfCommand = new LineOfCommand();
                    lineOfCommand.setQuantity(quantity);
                    lineOfCommand.setProduct(product);
                    product.setQuantityInStock(product.getQuantityInStock() - quantity);
                    BigDecimal total = product.getWeightedAveragePrice().multiply(BigDecimal.valueOf(quantity));
                    lineOfCommand.setTotal(total);
                    panier.addLinesCommand(lineOfCommand);
                    log.debug("Request to update : {}", product);
                    productRepository.save(product);
                }}
            return panierMapper.toDto(panier);
        }
        return null;
    }




    public void viderPanier() {
        Panier panier = getCurrentUserPanier();
        panier.getLinesCommands().clear();
        panierRepository.save(panier);
    }

    private Panier getCurrentUserPanier() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        Panier panier = (Panier) session.getAttribute("panier");
        if (panier == null) {
            panier = new Panier();
            session.setAttribute("panier", panier);
        }
        return panier;
    }

    /**
     * Update a panier.
     *
     * @param panierDTO the entity to save.
     * @return the persisted entity.
     */
    public PanierDTO update(PanierDTO panierDTO) {
        log.debug("Request to update Panier : {}", panierDTO);
        Panier panier = panierMapper.toEntity(panierDTO);
        panier = panierRepository.save(panier);
        return panierMapper.toDto(panier);
    }

    /**
     * Partially update a panier.
     *
     * @param panierDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PanierDTO> partialUpdate(PanierDTO panierDTO) {
        log.debug("Request to partially update Panier : {}", panierDTO);

        return panierRepository
            .findById(panierDTO.getId())
            .map(existingPanier -> {
                panierMapper.partialUpdate(existingPanier, panierDTO);

                return existingPanier;
            })
            .map(panierRepository::save)
            .map(panierMapper::toDto);
    }

    /**
     * Get all the paniers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PanierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Paniers");
        return panierRepository.findAll(pageable).map(panierMapper::toDto);
    }

    /**
     * Get one panier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PanierDTO> findOne(Long id) {
        log.debug("Request to get Panier : {}", id);
        return panierRepository.findById(id).map(panierMapper::toDto);
    }

    /**
     * Delete the panier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Panier : {}", id);
        panierRepository.deleteById(id);
    }
}
