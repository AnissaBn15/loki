package com.loki.web.rest;

import static com.loki.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.loki.IntegrationTest;
import com.loki.domain.Panier;
import com.loki.domain.enumeration.PanierStatus;
import com.loki.repository.PanierRepository;
import com.loki.service.dto.PanierDTO;
import com.loki.service.mapper.PanierMapper;
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
 * Integration tests for the {@link PanierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PanierResourceIT {

    private static final PanierStatus DEFAULT_STATUS = PanierStatus.VALIDE;
    private static final PanierStatus UPDATED_STATUS = PanierStatus.EN_COURS;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/paniers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private PanierMapper panierMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPanierMockMvc;

    private Panier panier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Panier createEntity(EntityManager em) {
        Panier panier = new Panier()
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED)
            .createdBy(DEFAULT_CREATED_BY)
            .updated(DEFAULT_UPDATED)
            .updatedBy(DEFAULT_UPDATED_BY);
        return panier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Panier createUpdatedEntity(EntityManager em) {
        Panier panier = new Panier()
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);
        return panier;
    }

    @BeforeEach
    public void initTest() {
        panier = createEntity(em);
    }

    @Test
    @Transactional
    void createPanier() throws Exception {
        int databaseSizeBeforeCreate = panierRepository.findAll().size();
        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);
        restPanierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(panierDTO)))
            .andExpect(status().isCreated());

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll();
        assertThat(panierList).hasSize(databaseSizeBeforeCreate + 1);
        Panier testPanier = panierList.get(panierList.size() - 1);
        assertThat(testPanier.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPanier.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testPanier.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPanier.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testPanier.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createPanierWithExistingId() throws Exception {
        // Create the Panier with an existing ID
        panier.setId(1L);
        PanierDTO panierDTO = panierMapper.toDto(panier);

        int databaseSizeBeforeCreate = panierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPanierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(panierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll();
        assertThat(panierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaniers() throws Exception {
        // Initialize the database
        panierRepository.saveAndFlush(panier);

        // Get all the panierList
        restPanierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(panier.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getPanier() throws Exception {
        // Initialize the database
        panierRepository.saveAndFlush(panier);

        // Get the panier
        restPanierMockMvc
            .perform(get(ENTITY_API_URL_ID, panier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(panier.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingPanier() throws Exception {
        // Get the panier
        restPanierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPanier() throws Exception {
        // Initialize the database
        panierRepository.saveAndFlush(panier);

        int databaseSizeBeforeUpdate = panierRepository.findAll().size();

        // Update the panier
        Panier updatedPanier = panierRepository.findById(panier.getId()).get();
        // Disconnect from session so that the updates on updatedPanier are not directly saved in db
        em.detach(updatedPanier);
        updatedPanier
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);
        PanierDTO panierDTO = panierMapper.toDto(updatedPanier);

        restPanierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, panierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(panierDTO))
            )
            .andExpect(status().isOk());

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
        Panier testPanier = panierList.get(panierList.size() - 1);
        assertThat(testPanier.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPanier.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPanier.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPanier.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testPanier.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingPanier() throws Exception {
        int databaseSizeBeforeUpdate = panierRepository.findAll().size();
        panier.setId(count.incrementAndGet());

        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPanierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, panierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(panierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPanier() throws Exception {
        int databaseSizeBeforeUpdate = panierRepository.findAll().size();
        panier.setId(count.incrementAndGet());

        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPanierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(panierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPanier() throws Exception {
        int databaseSizeBeforeUpdate = panierRepository.findAll().size();
        panier.setId(count.incrementAndGet());

        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPanierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(panierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePanierWithPatch() throws Exception {
        // Initialize the database
        panierRepository.saveAndFlush(panier);

        int databaseSizeBeforeUpdate = panierRepository.findAll().size();

        // Update the panier using partial update
        Panier partialUpdatedPanier = new Panier();
        partialUpdatedPanier.setId(panier.getId());

        partialUpdatedPanier.createdBy(UPDATED_CREATED_BY).updated(UPDATED_UPDATED).updatedBy(UPDATED_UPDATED_BY);

        restPanierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPanier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPanier))
            )
            .andExpect(status().isOk());

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
        Panier testPanier = panierList.get(panierList.size() - 1);
        assertThat(testPanier.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPanier.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testPanier.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPanier.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testPanier.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdatePanierWithPatch() throws Exception {
        // Initialize the database
        panierRepository.saveAndFlush(panier);

        int databaseSizeBeforeUpdate = panierRepository.findAll().size();

        // Update the panier using partial update
        Panier partialUpdatedPanier = new Panier();
        partialUpdatedPanier.setId(panier.getId());

        partialUpdatedPanier
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);

        restPanierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPanier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPanier))
            )
            .andExpect(status().isOk());

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
        Panier testPanier = panierList.get(panierList.size() - 1);
        assertThat(testPanier.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPanier.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPanier.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPanier.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testPanier.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingPanier() throws Exception {
        int databaseSizeBeforeUpdate = panierRepository.findAll().size();
        panier.setId(count.incrementAndGet());

        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPanierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, panierDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(panierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPanier() throws Exception {
        int databaseSizeBeforeUpdate = panierRepository.findAll().size();
        panier.setId(count.incrementAndGet());

        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPanierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(panierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPanier() throws Exception {
        int databaseSizeBeforeUpdate = panierRepository.findAll().size();
        panier.setId(count.incrementAndGet());

        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPanierMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(panierDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePanier() throws Exception {
        // Initialize the database
        panierRepository.saveAndFlush(panier);

        int databaseSizeBeforeDelete = panierRepository.findAll().size();

        // Delete the panier
        restPanierMockMvc
            .perform(delete(ENTITY_API_URL_ID, panier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Panier> panierList = panierRepository.findAll();
        assertThat(panierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
