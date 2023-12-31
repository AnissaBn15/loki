package com.loki.web.rest;

import com.loki.repository.PanierRepository;
import com.loki.service.PanierService;
import com.loki.service.dto.PanierDTO;
import com.loki.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.loki.domain.Panier}.
 */
/*@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class PanierResource {

    private final Logger log = LoggerFactory.getLogger(PanierResource.class);

    private static final String ENTITY_NAME = "panier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PanierService panierService;

    private final PanierRepository panierRepository;

    public PanierResource(PanierService panierService, PanierRepository panierRepository) {
        this.panierService = panierService;
        this.panierRepository = panierRepository;
    }

    /**
     * {@code POST  /paniers} : Create a new panier.
     *
     * @param panierDTO the panierDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new panierDTO, or with status {@code 400 (Bad Request)} if the panier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PostMapping("/paniers")
    public ResponseEntity<PanierDTO> createPanier(@RequestBody PanierDTO panierDTO) throws URISyntaxException {
        log.debug("REST request to save Panier : {}", panierDTO);
        if (panierDTO.getId() != null) {
            throw new BadRequestAlertException("A new panier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PanierDTO result = panierService.save(panierDTO);
        return ResponseEntity
            .created(new URI("/api/paniers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/
    /*@PostMapping("/paniers/addToPanier/{id}/{quantity}")
    public ResponseEntity<Void> addToPanier(Long clientId, @PathVariable Long id, @PathVariable int quantity) {
        panierService.createPanierForClient(id,quantity);
        return ResponseEntity.ok().build();
    }*/

    /*@DeleteMapping("/paniers/vider")
    public ResponseEntity<Void> viderPanier() {
        panierService.viderPanier();
        return ResponseEntity.ok().build();
    }*/
    /**
     * {@code PUT  /paniers/:id} : Updates an existing panier.
     *
     * @param id the id of the panierDTO to save.
     * @param panierDTO the panierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated panierDTO,
     * or with status {@code 400 (Bad Request)} if the panierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the panierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PutMapping("/paniers/{id}")
    public ResponseEntity<PanierDTO> updatePanier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PanierDTO panierDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Panier : {}, {}", id, panierDTO);
        if (panierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, panierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!panierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PanierDTO result = panierService.update(panierDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, panierDTO.getId().toString()))
            .body(result);
    }*/

    /**
     * {@code PATCH  /paniers/:id} : Partial updates given fields of an existing panier, field will ignore if it is null
     *
     * @param id the id of the panierDTO to save.
     * @param panierDTO the panierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated panierDTO,
     * or with status {@code 400 (Bad Request)} if the panierDTO is not valid,
     * or with status {@code 404 (Not Found)} if the panierDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the panierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PatchMapping(value = "/paniers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PanierDTO> partialUpdatePanier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PanierDTO panierDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Panier partially : {}, {}", id, panierDTO);
        if (panierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, panierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!panierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PanierDTO> result = panierService.partialUpdate(panierDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, panierDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /paniers} : get all the paniers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paniers in body.
     */
    /*@GetMapping("/paniers")
    public ResponseEntity<List<PanierDTO>> getAllPaniers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Paniers");
        Page<PanierDTO> page = panierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }*/

    /**
     * {@code GET  /paniers/:id} : get the "id" panier.
     *
     * @param id the id of the panierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the panierDTO, or with status {@code 404 (Not Found)}.
     */
    /*@GetMapping("/paniers/{id}")
    public ResponseEntity<PanierDTO> getPanier(@PathVariable Long id) {
        log.debug("REST request to get Panier : {}", id);
        Optional<PanierDTO> panierDTO = panierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(panierDTO);
    }*/

    /**
     * {@code DELETE  /paniers/:id} : delete the "id" panier.
     *
     * @param id the id of the panierDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    /*@DeleteMapping("/paniers/{id}")
    public ResponseEntity<Void> deletePanier(@PathVariable Long id) {
        log.debug("REST request to delete Panier : {}", id);
        panierService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}*/
