package com.aconcaguasf.cafeteros.service.impl;

import com.aconcaguasf.cafeteros.domain.Inventory;
import com.aconcaguasf.cafeteros.repository.InventoryRepository;
import com.aconcaguasf.cafeteros.service.InventoryService;
import com.aconcaguasf.cafeteros.service.dto.InventoryDTO;
import com.aconcaguasf.cafeteros.service.mapper.InventoryMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Inventory}.
 */
@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository inventoryRepository;

    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public InventoryDTO save(InventoryDTO inventoryDTO) {
        log.debug("Request to save Inventory : {}", inventoryDTO);
        Inventory inventory = inventoryMapper.toEntity(inventoryDTO);
        inventory = inventoryRepository.save(inventory);
        return inventoryMapper.toDto(inventory);
    }

    @Override
    public Optional<InventoryDTO> partialUpdate(InventoryDTO inventoryDTO) {
        log.debug("Request to partially update Inventory : {}", inventoryDTO);

        return inventoryRepository
            .findById(inventoryDTO.getId())
            .map(existingInventory -> {
                inventoryMapper.partialUpdate(existingInventory, inventoryDTO);

                return existingInventory;
            })
            .map(inventoryRepository::save)
            .map(inventoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Inventories");
        return inventoryRepository.findAll(pageable).map(inventoryMapper::toDto);
    }

    public Page<InventoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return inventoryRepository.findAllWithEagerRelationships(pageable).map(inventoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InventoryDTO> findOne(UUID id) {
        log.debug("Request to get Inventory : {}", id);
        return inventoryRepository.findOneWithEagerRelationships(id).map(inventoryMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Inventory : {}", id);
        inventoryRepository.deleteById(id);
    }
}
