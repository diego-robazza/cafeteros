package com.aconcaguasf.cafeteros.service;

import com.aconcaguasf.cafeteros.service.dto.EnteDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.aconcaguasf.cafeteros.domain.Ente}.
 */
public interface EnteService {
    /**
     * Save a ente.
     *
     * @param enteDTO the entity to save.
     * @return the persisted entity.
     */
    EnteDTO save(EnteDTO enteDTO);

    /**
     * Partially updates a ente.
     *
     * @param enteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EnteDTO> partialUpdate(EnteDTO enteDTO);

    /**
     * Get all the entes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EnteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EnteDTO> findOne(UUID id);

    /**
     * Delete the "id" ente.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
