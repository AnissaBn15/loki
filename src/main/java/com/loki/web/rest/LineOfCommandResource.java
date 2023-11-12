package com.loki.web.rest;

import com.loki.repository.LineOfCommandRepository;
import com.loki.service.LineOfCommandService;
import com.loki.service.dto.LineOfCommandDTO;
import com.loki.service.dto.ProductDTO;
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
 * REST controller for managing {@link com.loki.domain.LineOfCommand}.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class LineOfCommandResource {

    private final Logger log = LoggerFactory.getLogger(LineOfCommandResource.class);

    private static final String ENTITY_NAME = "lineOfCommand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LineOfCommandService lineOfCommandService;

    private final LineOfCommandRepository lineOfCommandRepository;

    public LineOfCommandResource(LineOfCommandService lineOfCommandService, LineOfCommandRepository lineOfCommandRepository) {
        this.lineOfCommandService = lineOfCommandService;
        this.lineOfCommandRepository = lineOfCommandRepository;
    }

    /**
     * {@code POST  /line-of-commands} : Create a new lineOfCommand.
     *
     * @param lineOfCommandDTO the lineOfCommandDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lineOfCommandDTO, or with status {@code 400 (Bad Request)} if the lineOfCommand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/line-of-commands")
    public ResponseEntity<LineOfCommandDTO> createLineOfCommand(@RequestBody LineOfCommandDTO lineOfCommandDTO, ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request to save LineOfCommand : {}", lineOfCommandDTO);
        if (lineOfCommandDTO.getId() != null) {
            throw new BadRequestAlertException("A new lineOfCommand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LineOfCommandDTO result = lineOfCommandService.save(lineOfCommandDTO);
        return ResponseEntity
            .created(new URI("/api/line-of-commands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /line-of-commands/:id} : Updates an existing lineOfCommand.
     *
     * @param id the id of the lineOfCommandDTO to save.
     * @param lineOfCommandDTO the lineOfCommandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineOfCommandDTO,
     * or with status {@code 400 (Bad Request)} if the lineOfCommandDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lineOfCommandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/line-of-commands/{id}")
    public ResponseEntity<LineOfCommandDTO> updateLineOfCommand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LineOfCommandDTO lineOfCommandDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LineOfCommand : {}, {}", id, lineOfCommandDTO);
        if (lineOfCommandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lineOfCommandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lineOfCommandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LineOfCommandDTO result = lineOfCommandService.update(lineOfCommandDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lineOfCommandDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /line-of-commands/:id} : Partial updates given fields of an existing lineOfCommand, field will ignore if it is null
     *
     * @param id the id of the lineOfCommandDTO to save.
     * @param lineOfCommandDTO the lineOfCommandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineOfCommandDTO,
     * or with status {@code 400 (Bad Request)} if the lineOfCommandDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lineOfCommandDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lineOfCommandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/line-of-commands/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LineOfCommandDTO> partialUpdateLineOfCommand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LineOfCommandDTO lineOfCommandDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LineOfCommand partially : {}, {}", id, lineOfCommandDTO);
        if (lineOfCommandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lineOfCommandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lineOfCommandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LineOfCommandDTO> result = lineOfCommandService.partialUpdate(lineOfCommandDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lineOfCommandDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /line-of-commands} : get all the lineOfCommands.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lineOfCommands in body.
     */
    @GetMapping("/line-of-commands")
    public ResponseEntity<List<LineOfCommandDTO>> getAllLineOfCommands(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LineOfCommands");
        Page<LineOfCommandDTO> page = lineOfCommandService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /line-of-commands/:id} : get the "id" lineOfCommand.
     *
     * @param id the id of the lineOfCommandDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lineOfCommandDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/line-of-commands/{id}")
    public ResponseEntity<LineOfCommandDTO> getLineOfCommand(@PathVariable Long id) {
        log.debug("REST request to get LineOfCommand : {}", id);
        Optional<LineOfCommandDTO> lineOfCommandDTO = lineOfCommandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lineOfCommandDTO);
    }

    /**
     * {@code DELETE  /line-of-commands/:id} : delete the "id" lineOfCommand.
     *
     * @param id the id of the lineOfCommandDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/line-of-commands/{id}")
    public ResponseEntity<Void> deleteLineOfCommand(@PathVariable Long id) {
        log.debug("REST request to delete LineOfCommand : {}", id);
        lineOfCommandService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
