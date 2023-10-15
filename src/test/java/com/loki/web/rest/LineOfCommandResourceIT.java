package com.loki.web.rest;

import static com.loki.web.rest.TestUtil.sameInstant;
import static com.loki.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.loki.IntegrationTest;
import com.loki.domain.LineOfCommand;
import com.loki.repository.LineOfCommandRepository;
import com.loki.service.dto.LineOfCommandDTO;
import com.loki.service.mapper.LineOfCommandMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LineOfCommandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LineOfCommandResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(2);

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/line-of-commands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LineOfCommandRepository lineOfCommandRepository;

    @Autowired
    private LineOfCommandMapper lineOfCommandMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLineOfCommandMockMvc;

    private LineOfCommand lineOfCommand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LineOfCommand createEntity(EntityManager em) {
        LineOfCommand lineOfCommand = new LineOfCommand()
            .quantity(DEFAULT_QUANTITY)
            .total(DEFAULT_TOTAL)
            .created(DEFAULT_CREATED)
            .createdBy(DEFAULT_CREATED_BY)
            .updated(DEFAULT_UPDATED)
            .updatedBy(DEFAULT_UPDATED_BY);
        return lineOfCommand;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LineOfCommand createUpdatedEntity(EntityManager em) {
        LineOfCommand lineOfCommand = new LineOfCommand()
            .quantity(UPDATED_QUANTITY)
            .total(UPDATED_TOTAL)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);
        return lineOfCommand;
    }

    @BeforeEach
    public void initTest() {
        lineOfCommand = createEntity(em);
    }

    @Test
    @Transactional
    void createLineOfCommand() throws Exception {
        int databaseSizeBeforeCreate = lineOfCommandRepository.findAll().size();
        // Create the LineOfCommand
        LineOfCommandDTO lineOfCommandDTO = lineOfCommandMapper.toDto(lineOfCommand);
        restLineOfCommandMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineOfCommandDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LineOfCommand in the database
        List<LineOfCommand> lineOfCommandList = lineOfCommandRepository.findAll();
        assertThat(lineOfCommandList).hasSize(databaseSizeBeforeCreate + 1);
        LineOfCommand testLineOfCommand = lineOfCommandList.get(lineOfCommandList.size() - 1);
        assertThat(testLineOfCommand.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testLineOfCommand.getTotal()).isEqualByComparingTo(DEFAULT_TOTAL);
        assertThat(testLineOfCommand.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testLineOfCommand.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testLineOfCommand.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testLineOfCommand.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createLineOfCommandWithExistingId() throws Exception {
        // Create the LineOfCommand with an existing ID
        lineOfCommand.setId(1L);
        LineOfCommandDTO lineOfCommandDTO = lineOfCommandMapper.toDto(lineOfCommand);

        int databaseSizeBeforeCreate = lineOfCommandRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineOfCommandMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineOfCommandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineOfCommand in the database
        List<LineOfCommand> lineOfCommandList = lineOfCommandRepository.findAll();
        assertThat(lineOfCommandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLineOfCommands() throws Exception {
        // Initialize the database
        lineOfCommandRepository.saveAndFlush(lineOfCommand);

        // Get all the lineOfCommandList
        restLineOfCommandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineOfCommand.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getLineOfCommand() throws Exception {
        // Initialize the database
        lineOfCommandRepository.saveAndFlush(lineOfCommand);

        // Get the lineOfCommand
        restLineOfCommandMockMvc
            .perform(get(ENTITY_API_URL_ID, lineOfCommand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lineOfCommand.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.total").value(sameNumber(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingLineOfCommand() throws Exception {
        // Get the lineOfCommand
        restLineOfCommandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLineOfCommand() throws Exception {
        // Initialize the database
        lineOfCommandRepository.saveAndFlush(lineOfCommand);

        int databaseSizeBeforeUpdate = lineOfCommandRepository.findAll().size();

        // Update the lineOfCommand
        LineOfCommand updatedLineOfCommand = lineOfCommandRepository.findById(lineOfCommand.getId()).get();
        // Disconnect from session so that the updates on updatedLineOfCommand are not directly saved in db
        em.detach(updatedLineOfCommand);
        updatedLineOfCommand
            .quantity(UPDATED_QUANTITY)
            .total(UPDATED_TOTAL)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);
        LineOfCommandDTO lineOfCommandDTO = lineOfCommandMapper.toDto(updatedLineOfCommand);

        restLineOfCommandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lineOfCommandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineOfCommandDTO))
            )
            .andExpect(status().isOk());

        // Validate the LineOfCommand in the database
        List<LineOfCommand> lineOfCommandList = lineOfCommandRepository.findAll();
        assertThat(lineOfCommandList).hasSize(databaseSizeBeforeUpdate);
        LineOfCommand testLineOfCommand = lineOfCommandList.get(lineOfCommandList.size() - 1);
        assertThat(testLineOfCommand.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testLineOfCommand.getTotal()).isEqualByComparingTo(UPDATED_TOTAL);
        assertThat(testLineOfCommand.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLineOfCommand.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLineOfCommand.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testLineOfCommand.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingLineOfCommand() throws Exception {
        int databaseSizeBeforeUpdate = lineOfCommandRepository.findAll().size();
        lineOfCommand.setId(count.incrementAndGet());

        // Create the LineOfCommand
        LineOfCommandDTO lineOfCommandDTO = lineOfCommandMapper.toDto(lineOfCommand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineOfCommandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lineOfCommandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineOfCommandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineOfCommand in the database
        List<LineOfCommand> lineOfCommandList = lineOfCommandRepository.findAll();
        assertThat(lineOfCommandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLineOfCommand() throws Exception {
        int databaseSizeBeforeUpdate = lineOfCommandRepository.findAll().size();
        lineOfCommand.setId(count.incrementAndGet());

        // Create the LineOfCommand
        LineOfCommandDTO lineOfCommandDTO = lineOfCommandMapper.toDto(lineOfCommand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineOfCommandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineOfCommandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineOfCommand in the database
        List<LineOfCommand> lineOfCommandList = lineOfCommandRepository.findAll();
        assertThat(lineOfCommandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLineOfCommand() throws Exception {
        int databaseSizeBeforeUpdate = lineOfCommandRepository.findAll().size();
        lineOfCommand.setId(count.incrementAndGet());

        // Create the LineOfCommand
        LineOfCommandDTO lineOfCommandDTO = lineOfCommandMapper.toDto(lineOfCommand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineOfCommandMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineOfCommandDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LineOfCommand in the database
        List<LineOfCommand> lineOfCommandList = lineOfCommandRepository.findAll();
        assertThat(lineOfCommandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLineOfCommandWithPatch() throws Exception {
        // Initialize the database
        lineOfCommandRepository.saveAndFlush(lineOfCommand);

        int databaseSizeBeforeUpdate = lineOfCommandRepository.findAll().size();

        // Update the lineOfCommand using partial update
        LineOfCommand partialUpdatedLineOfCommand = new LineOfCommand();
        partialUpdatedLineOfCommand.setId(lineOfCommand.getId());

        partialUpdatedLineOfCommand
            .quantity(UPDATED_QUANTITY)
            .total(UPDATED_TOTAL)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);

        restLineOfCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLineOfCommand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLineOfCommand))
            )
            .andExpect(status().isOk());

        // Validate the LineOfCommand in the database
        List<LineOfCommand> lineOfCommandList = lineOfCommandRepository.findAll();
        assertThat(lineOfCommandList).hasSize(databaseSizeBeforeUpdate);
        LineOfCommand testLineOfCommand = lineOfCommandList.get(lineOfCommandList.size() - 1);
        assertThat(testLineOfCommand.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testLineOfCommand.getTotal()).isEqualByComparingTo(UPDATED_TOTAL);
        assertThat(testLineOfCommand.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLineOfCommand.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testLineOfCommand.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testLineOfCommand.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateLineOfCommandWithPatch() throws Exception {
        // Initialize the database
        lineOfCommandRepository.saveAndFlush(lineOfCommand);

        int databaseSizeBeforeUpdate = lineOfCommandRepository.findAll().size();

        // Update the lineOfCommand using partial update
        LineOfCommand partialUpdatedLineOfCommand = new LineOfCommand();
        partialUpdatedLineOfCommand.setId(lineOfCommand.getId());

        partialUpdatedLineOfCommand
            .quantity(UPDATED_QUANTITY)
            .total(UPDATED_TOTAL)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);

        restLineOfCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLineOfCommand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLineOfCommand))
            )
            .andExpect(status().isOk());

        // Validate the LineOfCommand in the database
        List<LineOfCommand> lineOfCommandList = lineOfCommandRepository.findAll();
        assertThat(lineOfCommandList).hasSize(databaseSizeBeforeUpdate);
        LineOfCommand testLineOfCommand = lineOfCommandList.get(lineOfCommandList.size() - 1);
        assertThat(testLineOfCommand.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testLineOfCommand.getTotal()).isEqualByComparingTo(UPDATED_TOTAL);
        assertThat(testLineOfCommand.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testLineOfCommand.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLineOfCommand.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testLineOfCommand.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingLineOfCommand() throws Exception {
        int databaseSizeBeforeUpdate = lineOfCommandRepository.findAll().size();
        lineOfCommand.setId(count.incrementAndGet());

        // Create the LineOfCommand
        LineOfCommandDTO lineOfCommandDTO = lineOfCommandMapper.toDto(lineOfCommand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineOfCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lineOfCommandDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineOfCommandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineOfCommand in the database
        List<LineOfCommand> lineOfCommandList = lineOfCommandRepository.findAll();
        assertThat(lineOfCommandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLineOfCommand() throws Exception {
        int databaseSizeBeforeUpdate = lineOfCommandRepository.findAll().size();
        lineOfCommand.setId(count.incrementAndGet());

        // Create the LineOfCommand
        LineOfCommandDTO lineOfCommandDTO = lineOfCommandMapper.toDto(lineOfCommand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineOfCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineOfCommandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineOfCommand in the database
        List<LineOfCommand> lineOfCommandList = lineOfCommandRepository.findAll();
        assertThat(lineOfCommandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLineOfCommand() throws Exception {
        int databaseSizeBeforeUpdate = lineOfCommandRepository.findAll().size();
        lineOfCommand.setId(count.incrementAndGet());

        // Create the LineOfCommand
        LineOfCommandDTO lineOfCommandDTO = lineOfCommandMapper.toDto(lineOfCommand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineOfCommandMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineOfCommandDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LineOfCommand in the database
        List<LineOfCommand> lineOfCommandList = lineOfCommandRepository.findAll();
        assertThat(lineOfCommandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLineOfCommand() throws Exception {
        // Initialize the database
        lineOfCommandRepository.saveAndFlush(lineOfCommand);

        int databaseSizeBeforeDelete = lineOfCommandRepository.findAll().size();

        // Delete the lineOfCommand
        restLineOfCommandMockMvc
            .perform(delete(ENTITY_API_URL_ID, lineOfCommand.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LineOfCommand> lineOfCommandList = lineOfCommandRepository.findAll();
        assertThat(lineOfCommandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
