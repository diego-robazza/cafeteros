package com.aconcaguasf.cafeteros.service.mapper;

import com.aconcaguasf.cafeteros.domain.Ente;
import com.aconcaguasf.cafeteros.service.dto.EnteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ente} and its DTO {@link EnteDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EnteMapper extends EntityMapper<EnteDTO, Ente> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    EnteDTO toDtoName(Ente ente);
}
