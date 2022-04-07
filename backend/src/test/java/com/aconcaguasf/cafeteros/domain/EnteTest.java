package com.aconcaguasf.cafeteros.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.aconcaguasf.cafeteros.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EnteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ente.class);
        Ente ente1 = new Ente();
        ente1.setId(UUID.randomUUID());
        Ente ente2 = new Ente();
        ente2.setId(ente1.getId());
        assertThat(ente1).isEqualTo(ente2);
        ente2.setId(UUID.randomUUID());
        assertThat(ente1).isNotEqualTo(ente2);
        ente1.setId(null);
        assertThat(ente1).isNotEqualTo(ente2);
    }
}
