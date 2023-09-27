package com.loki.web.rest;

import static com.loki.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.loki.IntegrationTest;
import com.loki.domain.Client;
import com.loki.domain.enumeration.ClientType;
import com.loki.domain.enumeration.Role;
import com.loki.repository.ClientRepository;
import com.loki.service.dto.ClientDTO;
import com.loki.service.mapper.ClientMapper;
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
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final ClientType DEFAULT_TYPE = ClientType.PROFESSIONAL;
    private static final ClientType UPDATED_TYPE = ClientType.PARTICULAR;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Role DEFAULT_ROLE = Role.CLIENT;
    private static final Role UPDATED_ROLE = Role.FOURNISSEUR;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private Client client;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity(EntityManager em) {
        Client client = new Client()
            .code(DEFAULT_CODE)
            .type(DEFAULT_TYPE)
            .active(DEFAULT_ACTIVE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .bankAccountNumber(DEFAULT_BANK_ACCOUNT_NUMBER)
            .email(DEFAULT_EMAIL)
            .role(DEFAULT_ROLE)
            .address(DEFAULT_ADDRESS)
            .country(DEFAULT_COUNTRY)
            .created(DEFAULT_CREATED)
            .createdBy(DEFAULT_CREATED_BY)
            .updated(DEFAULT_UPDATED)
            .updatedBy(DEFAULT_UPDATED_BY);
        return client;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity(EntityManager em) {
        Client client = new Client()
            .code(UPDATED_CODE)
            .type(UPDATED_TYPE)
            .active(UPDATED_ACTIVE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .bankAccountNumber(UPDATED_BANK_ACCOUNT_NUMBER)
            .email(UPDATED_EMAIL)
            .role(UPDATED_ROLE)
            .address(UPDATED_ADDRESS)
            .country(UPDATED_COUNTRY)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);
        return client;
    }

    @BeforeEach
    public void initTest() {
        client = createEntity(em);
    }

    @Test
    @Transactional
    void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();
        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testClient.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testClient.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testClient.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testClient.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testClient.getBankAccountNumber()).isEqualTo(DEFAULT_BANK_ACCOUNT_NUMBER);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClient.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testClient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testClient.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testClient.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testClient.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testClient.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testClient.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createClientWithExistingId() throws Exception {
        // Create the Client with an existing ID
        client.setId(1L);
        ClientDTO clientDTO = clientMapper.toDto(client);

        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setCode(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setType(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setActive(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setCountry(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].bankAccountNumber").value(hasItem(DEFAULT_BANK_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc
            .perform(get(ENTITY_API_URL_ID, client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.bankAccountNumber").value(DEFAULT_BANK_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).get();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .code(UPDATED_CODE)
            .type(UPDATED_TYPE)
            .active(UPDATED_ACTIVE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .bankAccountNumber(UPDATED_BANK_ACCOUNT_NUMBER)
            .email(UPDATED_EMAIL)
            .role(UPDATED_ROLE)
            .address(UPDATED_ADDRESS)
            .country(UPDATED_COUNTRY)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);
        ClientDTO clientDTO = clientMapper.toDto(updatedClient);

        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testClient.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testClient.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testClient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testClient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testClient.getBankAccountNumber()).isEqualTo(UPDATED_BANK_ACCOUNT_NUMBER);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testClient.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testClient.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testClient.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testClient.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testClient.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testClient.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .code(UPDATED_CODE)
            .type(UPDATED_TYPE)
            .active(UPDATED_ACTIVE)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .country(UPDATED_COUNTRY)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testClient.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testClient.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testClient.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testClient.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testClient.getBankAccountNumber()).isEqualTo(DEFAULT_BANK_ACCOUNT_NUMBER);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testClient.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testClient.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testClient.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testClient.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testClient.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testClient.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .code(UPDATED_CODE)
            .type(UPDATED_TYPE)
            .active(UPDATED_ACTIVE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .bankAccountNumber(UPDATED_BANK_ACCOUNT_NUMBER)
            .email(UPDATED_EMAIL)
            .role(UPDATED_ROLE)
            .address(UPDATED_ADDRESS)
            .country(UPDATED_COUNTRY)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testClient.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testClient.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testClient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testClient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testClient.getBankAccountNumber()).isEqualTo(UPDATED_BANK_ACCOUNT_NUMBER);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testClient.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testClient.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testClient.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testClient.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testClient.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testClient.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Delete the client
        restClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, client.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
