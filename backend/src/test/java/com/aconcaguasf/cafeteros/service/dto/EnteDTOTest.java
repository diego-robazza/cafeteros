package com.aconcaguasf.cafeteros.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aconcaguasf.cafeteros.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EnteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnteDTO.class);
        EnteDTO enteDTO1 = new EnteDTO();
        enteDTO1.setId(UUID.randomUUID());
        EnteDTO enteDTO2 = new EnteDTO();
        assertThat(enteDTO1).isNotEqualTo(enteDTO2);
        enteDTO2.setId(enteDTO1.getId());
        assertThat(enteDTO1).isEqualTo(enteDTO2);
        enteDTO2.setId(UUID.randomUUID());
        assertThat(enteDTO1).isNotEqualTo(enteDTO2);
        enteDTO1.setId(null);
        assertThat(enteDTO1).isNotEqualTo(enteDTO2);
    }
}
