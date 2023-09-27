package com.loki.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.loki.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LineOfCommandDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineOfCommandDTO.class);
        LineOfCommandDTO lineOfCommandDTO1 = new LineOfCommandDTO();
        lineOfCommandDTO1.setId(1L);
        LineOfCommandDTO lineOfCommandDTO2 = new LineOfCommandDTO();
        assertThat(lineOfCommandDTO1).isNotEqualTo(lineOfCommandDTO2);
        lineOfCommandDTO2.setId(lineOfCommandDTO1.getId());
        assertThat(lineOfCommandDTO1).isEqualTo(lineOfCommandDTO2);
        lineOfCommandDTO2.setId(2L);
        assertThat(lineOfCommandDTO1).isNotEqualTo(lineOfCommandDTO2);
        lineOfCommandDTO1.setId(null);
        assertThat(lineOfCommandDTO1).isNotEqualTo(lineOfCommandDTO2);
    }
}
