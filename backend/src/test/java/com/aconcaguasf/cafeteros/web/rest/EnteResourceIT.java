package com.aconcaguasf.cafeteros.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aconcaguasf.cafeteros.IntegrationTest;
import com.aconcaguasf.cafeteros.domain.Ente;
import com.aconcaguasf.cafeteros.repository.EnteRepository;
import com.aconcaguasf.cafeteros.service.dto.EnteDTO;
import com.aconcaguasf.cafeteros.service.mapper.EnteMapper;
import java.util.List;
import java.util.UUID;
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
 * Integration tests for the {@link EnteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnteResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String ENTITY_API_URL = "/api/entes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private EnteRepository enteRepository;

    @Autowired
    private EnteMapper enteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnteMockMvc;

    private Ente ente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ente createEntity(EntityManager em) {
        Ente ente = new Ente().name(DEFAULT_NAME).activated(DEFAULT_ACTIVATED);
        return ente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ente createUpdatedEntity(EntityManager em) {
        Ente ente = new Ente().name(UPDATED_NAME).activated(UPDATED_ACTIVATED);
        return ente;
    }

    @BeforeEach
    public void initTest() {
        ente = createEntity(em);
    }

    @Test
    @Transactional
    void createEnte() throws Exception {
        int databaseSizeBeforeCreate = enteRepository.findAll().size();
        // Create the Ente
        EnteDTO enteDTO = enteMapper.toDto(ente);
        restEnteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enteDTO)))
            .andExpect(status().isCreated());

        // Validate the Ente in the database
        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeCreate + 1);
        Ente testEnte = enteList.get(enteList.size() - 1);
        assertThat(testEnte.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEnte.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
    }

    @Test
    @Transactional
    void createEnteWithExistingId() throws Exception {
        // Create the Ente with an existing ID
        enteRepository.saveAndFlush(ente);
        EnteDTO enteDTO = enteMapper.toDto(ente);

        int databaseSizeBeforeCreate = enteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ente in the database
        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = enteRepository.findAll().size();
        // set the field null
        ente.setName(null);

        // Create the Ente, which fails.
        EnteDTO enteDTO = enteMapper.toDto(ente);

        restEnteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enteDTO)))
            .andExpect(status().isBadRequest());

        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = enteRepository.findAll().size();
        // set the field null
        ente.setActivated(null);

        // Create the Ente, which fails.
        EnteDTO enteDTO = enteMapper.toDto(ente);

        restEnteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enteDTO)))
            .andExpect(status().isBadRequest());

        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEntes() throws Exception {
        // Initialize the database
        enteRepository.saveAndFlush(ente);

        // Get all the enteList
        restEnteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ente.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())));
    }

    @Test
    @Transactional
    void getEnte() throws Exception {
        // Initialize the database
        enteRepository.saveAndFlush(ente);

        // Get the ente
        restEnteMockMvc
            .perform(get(ENTITY_API_URL_ID, ente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ente.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEnte() throws Exception {
        // Get the ente
        restEnteMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEnte() throws Exception {
        // Initialize the database
        enteRepository.saveAndFlush(ente);

        int databaseSizeBeforeUpdate = enteRepository.findAll().size();

        // Update the ente
        Ente updatedEnte = enteRepository.findById(ente.getId()).get();
        // Disconnect from session so that the updates on updatedEnte are not directly saved in db
        em.detach(updatedEnte);
        updatedEnte.name(UPDATED_NAME).activated(UPDATED_ACTIVATED);
        EnteDTO enteDTO = enteMapper.toDto(updatedEnte);

        restEnteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ente in the database
        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeUpdate);
        Ente testEnte = enteList.get(enteList.size() - 1);
        assertThat(testEnte.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEnte.getActivated()).isEqualTo(UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void putNonExistingEnte() throws Exception {
        int databaseSizeBeforeUpdate = enteRepository.findAll().size();
        ente.setId(UUID.randomUUID());

        // Create the Ente
        EnteDTO enteDTO = enteMapper.toDto(ente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ente in the database
        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnte() throws Exception {
        int databaseSizeBeforeUpdate = enteRepository.findAll().size();
        ente.setId(UUID.randomUUID());

        // Create the Ente
        EnteDTO enteDTO = enteMapper.toDto(ente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ente in the database
        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnte() throws Exception {
        int databaseSizeBeforeUpdate = enteRepository.findAll().size();
        ente.setId(UUID.randomUUID());

        // Create the Ente
        EnteDTO enteDTO = enteMapper.toDto(ente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ente in the database
        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnteWithPatch() throws Exception {
        // Initialize the database
        enteRepository.saveAndFlush(ente);

        int databaseSizeBeforeUpdate = enteRepository.findAll().size();

        // Update the ente using partial update
        Ente partialUpdatedEnte = new Ente();
        partialUpdatedEnte.setId(ente.getId());

        partialUpdatedEnte.activated(UPDATED_ACTIVATED);

        restEnteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnte))
            )
            .andExpect(status().isOk());

        // Validate the Ente in the database
        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeUpdate);
        Ente testEnte = enteList.get(enteList.size() - 1);
        assertThat(testEnte.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEnte.getActivated()).isEqualTo(UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void fullUpdateEnteWithPatch() throws Exception {
        // Initialize the database
        enteRepository.saveAndFlush(ente);

        int databaseSizeBeforeUpdate = enteRepository.findAll().size();

        // Update the ente using partial update
        Ente partialUpdatedEnte = new Ente();
        partialUpdatedEnte.setId(ente.getId());

        partialUpdatedEnte.name(UPDATED_NAME).activated(UPDATED_ACTIVATED);

        restEnteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnte))
            )
            .andExpect(status().isOk());

        // Validate the Ente in the database
        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeUpdate);
        Ente testEnte = enteList.get(enteList.size() - 1);
        assertThat(testEnte.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEnte.getActivated()).isEqualTo(UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void patchNonExistingEnte() throws Exception {
        int databaseSizeBeforeUpdate = enteRepository.findAll().size();
        ente.setId(UUID.randomUUID());

        // Create the Ente
        EnteDTO enteDTO = enteMapper.toDto(ente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ente in the database
        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnte() throws Exception {
        int databaseSizeBeforeUpdate = enteRepository.findAll().size();
        ente.setId(UUID.randomUUID());

        // Create the Ente
        EnteDTO enteDTO = enteMapper.toDto(ente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ente in the database
        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnte() throws Exception {
        int databaseSizeBeforeUpdate = enteRepository.findAll().size();
        ente.setId(UUID.randomUUID());

        // Create the Ente
        EnteDTO enteDTO = enteMapper.toDto(ente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(enteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ente in the database
        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnte() throws Exception {
        // Initialize the database
        enteRepository.saveAndFlush(ente);

        int databaseSizeBeforeDelete = enteRepository.findAll().size();

        // Delete the ente
        restEnteMockMvc
            .perform(delete(ENTITY_API_URL_ID, ente.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ente> enteList = enteRepository.findAll();
        assertThat(enteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
