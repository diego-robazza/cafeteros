package com.aconcaguasf.cafeteros.web.rest;

import static com.aconcaguasf.cafeteros.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aconcaguasf.cafeteros.IntegrationTest;
import com.aconcaguasf.cafeteros.domain.Ente;
import com.aconcaguasf.cafeteros.domain.Inventory;
import com.aconcaguasf.cafeteros.domain.enumeration.AuditStatus;
import com.aconcaguasf.cafeteros.repository.InventoryRepository;
import com.aconcaguasf.cafeteros.service.dto.InventoryDTO;
import com.aconcaguasf.cafeteros.service.mapper.InventoryMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link InventoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InventoryResourceIT {

    private static final String DEFAULT_NAME_AUDIT = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AUDIT = "BBBBBBBBBB";

    private static final AuditStatus DEFAULT_AUDIT_STATUS = AuditStatus.PROCESO;
    private static final AuditStatus UPDATED_AUDIT_STATUS = AuditStatus.FINALIZADA;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/inventories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventoryMockMvc;

    private Inventory inventory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventory createEntity(EntityManager em) {
        Inventory inventory = new Inventory().nameAudit(DEFAULT_NAME_AUDIT).auditStatus(DEFAULT_AUDIT_STATUS).startDate(DEFAULT_START_DATE);
        // Add required entity
        Ente ente;
        if (TestUtil.findAll(em, Ente.class).isEmpty()) {
            ente = EnteResourceIT.createEntity(em);
            em.persist(ente);
            em.flush();
        } else {
            ente = TestUtil.findAll(em, Ente.class).get(0);
        }
        inventory.setEnte(ente);
        return inventory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventory createUpdatedEntity(EntityManager em) {
        Inventory inventory = new Inventory().nameAudit(UPDATED_NAME_AUDIT).auditStatus(UPDATED_AUDIT_STATUS).startDate(UPDATED_START_DATE);
        // Add required entity
        Ente ente;
        if (TestUtil.findAll(em, Ente.class).isEmpty()) {
            ente = EnteResourceIT.createUpdatedEntity(em);
            em.persist(ente);
            em.flush();
        } else {
            ente = TestUtil.findAll(em, Ente.class).get(0);
        }
        inventory.setEnte(ente);
        return inventory;
    }

    @BeforeEach
    public void initTest() {
        inventory = createEntity(em);
    }

    @Test
    @Transactional
    void createInventory() throws Exception {
        int databaseSizeBeforeCreate = inventoryRepository.findAll().size();
        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);
        restInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate + 1);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getNameAudit()).isEqualTo(DEFAULT_NAME_AUDIT);
        assertThat(testInventory.getAuditStatus()).isEqualTo(DEFAULT_AUDIT_STATUS);
        assertThat(testInventory.getStartDate()).isEqualTo(DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void createInventoryWithExistingId() throws Exception {
        // Create the Inventory with an existing ID
        inventoryRepository.saveAndFlush(inventory);
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        int databaseSizeBeforeCreate = inventoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameAuditIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().size();
        // set the field null
        inventory.setNameAudit(null);

        // Create the Inventory, which fails.
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        restInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isBadRequest());

        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInventories() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().toString())))
            .andExpect(jsonPath("$.[*].nameAudit").value(hasItem(DEFAULT_NAME_AUDIT)))
            .andExpect(jsonPath("$.[*].auditStatus").value(hasItem(DEFAULT_AUDIT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))));
    }

    @Test
    @Transactional
    void getInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get the inventory
        restInventoryMockMvc
            .perform(get(ENTITY_API_URL_ID, inventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventory.getId().toString()))
            .andExpect(jsonPath("$.nameAudit").value(DEFAULT_NAME_AUDIT))
            .andExpect(jsonPath("$.auditStatus").value(DEFAULT_AUDIT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingInventory() throws Exception {
        // Get the inventory
        restInventoryMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Update the inventory
        Inventory updatedInventory = inventoryRepository.findById(inventory.getId()).get();
        // Disconnect from session so that the updates on updatedInventory are not directly saved in db
        em.detach(updatedInventory);
        updatedInventory.nameAudit(UPDATED_NAME_AUDIT).auditStatus(UPDATED_AUDIT_STATUS).startDate(UPDATED_START_DATE);
        InventoryDTO inventoryDTO = inventoryMapper.toDto(updatedInventory);

        restInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getNameAudit()).isEqualTo(UPDATED_NAME_AUDIT);
        assertThat(testInventory.getAuditStatus()).isEqualTo(UPDATED_AUDIT_STATUS);
        assertThat(testInventory.getStartDate()).isEqualTo(UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void putNonExistingInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();
        inventory.setId(UUID.randomUUID());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();
        inventory.setId(UUID.randomUUID());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();
        inventory.setId(UUID.randomUUID());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventoryWithPatch() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Update the inventory using partial update
        Inventory partialUpdatedInventory = new Inventory();
        partialUpdatedInventory.setId(inventory.getId());

        partialUpdatedInventory.auditStatus(UPDATED_AUDIT_STATUS).startDate(UPDATED_START_DATE);

        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInventory))
            )
            .andExpect(status().isOk());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getNameAudit()).isEqualTo(DEFAULT_NAME_AUDIT);
        assertThat(testInventory.getAuditStatus()).isEqualTo(UPDATED_AUDIT_STATUS);
        assertThat(testInventory.getStartDate()).isEqualTo(UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void fullUpdateInventoryWithPatch() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Update the inventory using partial update
        Inventory partialUpdatedInventory = new Inventory();
        partialUpdatedInventory.setId(inventory.getId());

        partialUpdatedInventory.nameAudit(UPDATED_NAME_AUDIT).auditStatus(UPDATED_AUDIT_STATUS).startDate(UPDATED_START_DATE);

        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInventory))
            )
            .andExpect(status().isOk());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getNameAudit()).isEqualTo(UPDATED_NAME_AUDIT);
        assertThat(testInventory.getAuditStatus()).isEqualTo(UPDATED_AUDIT_STATUS);
        assertThat(testInventory.getStartDate()).isEqualTo(UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();
        inventory.setId(UUID.randomUUID());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();
        inventory.setId(UUID.randomUUID());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();
        inventory.setId(UUID.randomUUID());

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(inventoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        int databaseSizeBeforeDelete = inventoryRepository.findAll().size();

        // Delete the inventory
        restInventoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventory.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
