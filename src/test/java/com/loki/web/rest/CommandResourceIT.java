package com.loki.web.rest;

import static com.loki.web.rest.TestUtil.sameInstant;
import static com.loki.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.loki.IntegrationTest;
import com.loki.domain.Command;
import com.loki.domain.enumeration.CommandStatus;
import com.loki.repository.CommandRepository;
import com.loki.service.dto.CommandDTO;
import com.loki.service.mapper.CommandMapper;
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
 * Integration tests for the {@link CommandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommandResourceIT {

    private static final String DEFAULT_COMMAND_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_COMMAND_NUMBER = "BBBBBBBBBB";

    private static final CommandStatus DEFAULT_STATUS = CommandStatus.EN_ATTENTE_PAIEMENT;
    private static final CommandStatus UPDATED_STATUS = CommandStatus.PAIEMENT_ACCEPTE;

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

    private static final String ENTITY_API_URL = "/api/commands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private CommandMapper commandMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommandMockMvc;

    private Command command;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Command createEntity(EntityManager em) {
        Command command = new Command()
            .commandNumber(DEFAULT_COMMAND_NUMBER)
            .status(DEFAULT_STATUS)
            .total(DEFAULT_TOTAL)
            .created(DEFAULT_CREATED)
            .createdBy(DEFAULT_CREATED_BY)
            .updated(DEFAULT_UPDATED)
            .updatedBy(DEFAULT_UPDATED_BY);
        return command;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Command createUpdatedEntity(EntityManager em) {
        Command command = new Command()
            .commandNumber(UPDATED_COMMAND_NUMBER)
            .status(UPDATED_STATUS)
            .total(UPDATED_TOTAL)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);
        return command;
    }

    @BeforeEach
    public void initTest() {
        command = createEntity(em);
    }

    @Test
    @Transactional
    void createCommand() throws Exception {
        int databaseSizeBeforeCreate = commandRepository.findAll().size();
        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);
        restCommandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandDTO)))
            .andExpect(status().isCreated());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeCreate + 1);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getCommandNumber()).isEqualTo(DEFAULT_COMMAND_NUMBER);
        assertThat(testCommand.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCommand.getTotal()).isEqualByComparingTo(DEFAULT_TOTAL);
        assertThat(testCommand.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testCommand.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCommand.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testCommand.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createCommandWithExistingId() throws Exception {
        // Create the Command with an existing ID
        command.setId(1L);
        CommandDTO commandDTO = commandMapper.toDto(command);

        int databaseSizeBeforeCreate = commandRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommands() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList
        restCommandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(command.getId().intValue())))
            .andExpect(jsonPath("$.[*].commandNumber").value(hasItem(DEFAULT_COMMAND_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get the command
        restCommandMockMvc
            .perform(get(ENTITY_API_URL_ID, command.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(command.getId().intValue()))
            .andExpect(jsonPath("$.commandNumber").value(DEFAULT_COMMAND_NUMBER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.total").value(sameNumber(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingCommand() throws Exception {
        // Get the command
        restCommandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Update the command
        Command updatedCommand = commandRepository.findById(command.getId()).get();
        // Disconnect from session so that the updates on updatedCommand are not directly saved in db
        em.detach(updatedCommand);
        updatedCommand
            .commandNumber(UPDATED_COMMAND_NUMBER)
            .status(UPDATED_STATUS)
            .total(UPDATED_TOTAL)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);
        CommandDTO commandDTO = commandMapper.toDto(updatedCommand);

        restCommandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandDTO))
            )
            .andExpect(status().isOk());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getCommandNumber()).isEqualTo(UPDATED_COMMAND_NUMBER);
        assertThat(testCommand.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCommand.getTotal()).isEqualByComparingTo(UPDATED_TOTAL);
        assertThat(testCommand.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCommand.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCommand.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testCommand.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommandWithPatch() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Update the command using partial update
        Command partialUpdatedCommand = new Command();
        partialUpdatedCommand.setId(command.getId());

        partialUpdatedCommand
            .commandNumber(UPDATED_COMMAND_NUMBER)
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .updatedBy(UPDATED_UPDATED_BY);

        restCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommand))
            )
            .andExpect(status().isOk());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getCommandNumber()).isEqualTo(UPDATED_COMMAND_NUMBER);
        assertThat(testCommand.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCommand.getTotal()).isEqualByComparingTo(DEFAULT_TOTAL);
        assertThat(testCommand.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCommand.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCommand.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testCommand.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateCommandWithPatch() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Update the command using partial update
        Command partialUpdatedCommand = new Command();
        partialUpdatedCommand.setId(command.getId());

        partialUpdatedCommand
            .commandNumber(UPDATED_COMMAND_NUMBER)
            .status(UPDATED_STATUS)
            .total(UPDATED_TOTAL)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);

        restCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommand))
            )
            .andExpect(status().isOk());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getCommandNumber()).isEqualTo(UPDATED_COMMAND_NUMBER);
        assertThat(testCommand.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCommand.getTotal()).isEqualByComparingTo(UPDATED_TOTAL);
        assertThat(testCommand.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCommand.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCommand.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testCommand.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commandDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commandDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        int databaseSizeBeforeDelete = commandRepository.findAll().size();

        // Delete the command
        restCommandMockMvc
            .perform(delete(ENTITY_API_URL_ID, command.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
