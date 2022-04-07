package com.aconcaguasf.cafeteros.service.mapper;

import com.aconcaguasf.cafeteros.domain.Inventory;
import com.aconcaguasf.cafeteros.service.dto.InventoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inventory} and its DTO {@link InventoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { EnteMapper.class })
public interface InventoryMapper extends EntityMapper<InventoryDTO, Inventory> {
    @Mapping(target = "ente", source = "ente", qualifiedByName = "name")
    InventoryDTO toDto(Inventory s);
}
