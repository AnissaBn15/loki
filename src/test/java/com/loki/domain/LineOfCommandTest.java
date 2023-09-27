package com.loki.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.loki.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LineOfCommandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineOfCommand.class);
        LineOfCommand lineOfCommand1 = new LineOfCommand();
        lineOfCommand1.setId(1L);
        LineOfCommand lineOfCommand2 = new LineOfCommand();
        lineOfCommand2.setId(lineOfCommand1.getId());
        assertThat(lineOfCommand1).isEqualTo(lineOfCommand2);
        lineOfCommand2.setId(2L);
        assertThat(lineOfCommand1).isNotEqualTo(lineOfCommand2);
        lineOfCommand1.setId(null);
        assertThat(lineOfCommand1).isNotEqualTo(lineOfCommand2);
    }
}
