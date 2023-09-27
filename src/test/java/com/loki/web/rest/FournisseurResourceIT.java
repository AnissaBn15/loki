package com.loki.web.rest;

import static com.loki.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.loki.IntegrationTest;
import com.loki.domain.Fournisseur;
import com.loki.domain.enumeration.Role;
import com.loki.repository.FournisseurRepository;
import com.loki.service.dto.FournisseurDTO;
import com.loki.service.mapper.FournisseurMapper;
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
 * Integration tests for the {@link FournisseurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FournisseurResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/fournisseurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private FournisseurMapper fournisseurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFournisseurMockMvc;

    private Fournisseur fournisseur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fournisseur createEntity(EntityManager em) {
        Fournisseur fournisseur = new Fournisseur()
            .code(DEFAULT_CODE)
            .active(DEFAULT_ACTIVE)
            .company(DEFAULT_COMPANY)
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
        return fournisseur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fournisseur createUpdatedEntity(EntityManager em) {
        Fournisseur fournisseur = new Fournisseur()
            .code(UPDATED_CODE)
            .active(UPDATED_ACTIVE)
            .company(UPDATED_COMPANY)
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
        return fournisseur;
    }

    @BeforeEach
    public void initTest() {
        fournisseur = createEntity(em);
    }

    @Test
    @Transactional
    void createFournisseur() throws Exception {
        int databaseSizeBeforeCreate = fournisseurRepository.findAll().size();
        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);
        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeCreate + 1);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testFournisseur.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testFournisseur.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testFournisseur.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testFournisseur.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testFournisseur.getBankAccountNumber()).isEqualTo(DEFAULT_BANK_ACCOUNT_NUMBER);
        assertThat(testFournisseur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFournisseur.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testFournisseur.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testFournisseur.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testFournisseur.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testFournisseur.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testFournisseur.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testFournisseur.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createFournisseurWithExistingId() throws Exception {
        // Create the Fournisseur with an existing ID
        fournisseur.setId(1L);
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        int databaseSizeBeforeCreate = fournisseurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setCode(null);

        // Create the Fournisseur, which fails.
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setActive(null);

        // Create the Fournisseur, which fails.
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setCountry(null);

        // Create the Fournisseur, which fails.
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        restFournisseurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFournisseurs() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fournisseur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
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
    void getFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get the fournisseur
        restFournisseurMockMvc
            .perform(get(ENTITY_API_URL_ID, fournisseur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fournisseur.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY))
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
    void getNonExistingFournisseur() throws Exception {
        // Get the fournisseur
        restFournisseurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

        // Update the fournisseur
        Fournisseur updatedFournisseur = fournisseurRepository.findById(fournisseur.getId()).get();
        // Disconnect from session so that the updates on updatedFournisseur are not directly saved in db
        em.detach(updatedFournisseur);
        updatedFournisseur
            .code(UPDATED_CODE)
            .active(UPDATED_ACTIVE)
            .company(UPDATED_COMPANY)
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
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(updatedFournisseur);

        restFournisseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fournisseurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFournisseur.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testFournisseur.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testFournisseur.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testFournisseur.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testFournisseur.getBankAccountNumber()).isEqualTo(UPDATED_BANK_ACCOUNT_NUMBER);
        assertThat(testFournisseur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFournisseur.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testFournisseur.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testFournisseur.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testFournisseur.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFournisseur.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testFournisseur.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testFournisseur.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fournisseurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fournisseurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFournisseurWithPatch() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

        // Update the fournisseur using partial update
        Fournisseur partialUpdatedFournisseur = new Fournisseur();
        partialUpdatedFournisseur.setId(fournisseur.getId());

        partialUpdatedFournisseur
            .active(UPDATED_ACTIVE)
            .company(UPDATED_COMPANY)
            .firstName(UPDATED_FIRST_NAME)
            .address(UPDATED_ADDRESS)
            .country(UPDATED_COUNTRY)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFournisseur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFournisseur))
            )
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testFournisseur.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testFournisseur.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testFournisseur.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testFournisseur.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testFournisseur.getBankAccountNumber()).isEqualTo(DEFAULT_BANK_ACCOUNT_NUMBER);
        assertThat(testFournisseur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFournisseur.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testFournisseur.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testFournisseur.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testFournisseur.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFournisseur.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testFournisseur.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testFournisseur.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateFournisseurWithPatch() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

        // Update the fournisseur using partial update
        Fournisseur partialUpdatedFournisseur = new Fournisseur();
        partialUpdatedFournisseur.setId(fournisseur.getId());

        partialUpdatedFournisseur
            .code(UPDATED_CODE)
            .active(UPDATED_ACTIVE)
            .company(UPDATED_COMPANY)
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

        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFournisseur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFournisseur))
            )
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFournisseur.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testFournisseur.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testFournisseur.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testFournisseur.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testFournisseur.getBankAccountNumber()).isEqualTo(UPDATED_BANK_ACCOUNT_NUMBER);
        assertThat(testFournisseur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFournisseur.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testFournisseur.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testFournisseur.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testFournisseur.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFournisseur.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testFournisseur.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testFournisseur.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fournisseurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();
        fournisseur.setId(count.incrementAndGet());

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFournisseurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fournisseurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeDelete = fournisseurRepository.findAll().size();

        // Delete the fournisseur
        restFournisseurMockMvc
            .perform(delete(ENTITY_API_URL_ID, fournisseur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
