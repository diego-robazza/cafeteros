package com.aconcaguasf.cafeteros.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.aconcaguasf.cafeteros.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InventoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventory.class);
        Inventory inventory1 = new Inventory();
        inventory1.setId(UUID.randomUUID());
        Inventory inventory2 = new Inventory();
        inventory2.setId(inventory1.getId());
        assertThat(inventory1).isEqualTo(inventory2);
        inventory2.setId(UUID.randomUUID());
        assertThat(inventory1).isNotEqualTo(inventory2);
        inventory1.setId(null);
        assertThat(inventory1).isNotEqualTo(inventory2);
    }
}
