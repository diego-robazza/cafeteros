package com.aconcaguasf.cafeteros.web.rest;

import com.aconcaguasf.cafeteros.repository.EnteRepository;
import com.aconcaguasf.cafeteros.service.EnteService;
import com.aconcaguasf.cafeteros.service.dto.EnteDTO;
import com.aconcaguasf.cafeteros.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.aconcaguasf.cafeteros.domain.Ente}.
 */
@RestController
@RequestMapping("/api")
public class EnteResource {

    private final Logger log = LoggerFactory.getLogger(EnteResource.class);

    private static final String ENTITY_NAME = "ente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnteService enteService;

    private final EnteRepository enteRepository;

    public EnteResource(EnteService enteService, EnteRepository enteRepository) {
        this.enteService = enteService;
        this.enteRepository = enteRepository;
    }

    /**
     * {@code POST  /entes} : Create a new ente.
     *
     * @param enteDTO the enteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enteDTO, or with status {@code 400 (Bad Request)} if the ente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entes")
    public ResponseEntity<EnteDTO> createEnte(@Valid @RequestBody EnteDTO enteDTO) throws URISyntaxException {
        log.debug("REST request to save Ente : {}", enteDTO);
        if (enteDTO.getId() != null) {
            throw new BadRequestAlertException("A new ente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EnteDTO result = enteService.save(enteDTO);
        return ResponseEntity
            .created(new URI("/api/entes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entes/:id} : Updates an existing ente.
     *
     * @param id the id of the enteDTO to save.
     * @param enteDTO the enteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enteDTO,
     * or with status {@code 400 (Bad Request)} if the enteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entes/{id}")
    public ResponseEntity<EnteDTO> updateEnte(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody EnteDTO enteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ente : {}, {}", id, enteDTO);
        if (enteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EnteDTO result = enteService.save(enteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, enteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /entes/:id} : Partial updates given fields of an existing ente, field will ignore if it is null
     *
     * @param id the id of the enteDTO to save.
     * @param enteDTO the enteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enteDTO,
     * or with status {@code 400 (Bad Request)} if the enteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the enteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the enteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/entes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EnteDTO> partialUpdateEnte(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody EnteDTO enteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ente partially : {}, {}", id, enteDTO);
        if (enteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EnteDTO> result = enteService.partialUpdate(enteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, enteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /entes} : get all the entes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entes in body.
     */
    @GetMapping("/entes")
    public ResponseEntity<List<EnteDTO>> getAllEntes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Entes");
        Page<EnteDTO> page = enteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /entes/:id} : get the "id" ente.
     *
     * @param id the id of the enteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entes/{id}")
    public ResponseEntity<EnteDTO> getEnte(@PathVariable UUID id) {
        log.debug("REST request to get Ente : {}", id);
        Optional<EnteDTO> enteDTO = enteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(enteDTO);
    }

    /**
     * {@code DELETE  /entes/:id} : delete the "id" ente.
     *
     * @param id the id of the enteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entes/{id}")
    public ResponseEntity<Void> deleteEnte(@PathVariable UUID id) {
        log.debug("REST request to delete Ente : {}", id);
        enteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
