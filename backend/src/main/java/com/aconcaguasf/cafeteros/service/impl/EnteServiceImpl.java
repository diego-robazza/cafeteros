package com.aconcaguasf.cafeteros.service.impl;

import com.aconcaguasf.cafeteros.domain.Ente;
import com.aconcaguasf.cafeteros.repository.EnteRepository;
import com.aconcaguasf.cafeteros.service.EnteService;
import com.aconcaguasf.cafeteros.service.dto.EnteDTO;
import com.aconcaguasf.cafeteros.service.mapper.EnteMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ente}.
 */
@Service
@Transactional
public class EnteServiceImpl implements EnteService {

    private final Logger log = LoggerFactory.getLogger(EnteServiceImpl.class);

    private final EnteRepository enteRepository;

    private final EnteMapper enteMapper;

    public EnteServiceImpl(EnteRepository enteRepository, EnteMapper enteMapper) {
        this.enteRepository = enteRepository;
        this.enteMapper = enteMapper;
    }

    @Override
    public EnteDTO save(EnteDTO enteDTO) {
        log.debug("Request to save Ente : {}", enteDTO);
        Ente ente = enteMapper.toEntity(enteDTO);
        ente = enteRepository.save(ente);
        return enteMapper.toDto(ente);
    }

    @Override
    public Optional<EnteDTO> partialUpdate(EnteDTO enteDTO) {
        log.debug("Request to partially update Ente : {}", enteDTO);

        return enteRepository
            .findById(enteDTO.getId())
            .map(existingEnte -> {
                enteMapper.partialUpdate(existingEnte, enteDTO);

                return existingEnte;
            })
            .map(enteRepository::save)
            .map(enteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Entes");
        return enteRepository.findAll(pageable).map(enteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnteDTO> findOne(UUID id) {
        log.debug("Request to get Ente : {}", id);
        return enteRepository.findById(id).map(enteMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Ente : {}", id);
        enteRepository.deleteById(id);
    }
}
