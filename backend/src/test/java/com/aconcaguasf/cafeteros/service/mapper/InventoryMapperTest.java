package com.aconcaguasf.cafeteros.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventoryMapperTest {

    private InventoryMapper inventoryMapper;

    @BeforeEach
    public void setUp() {
        inventoryMapper = new InventoryMapperImpl();
    }
}
