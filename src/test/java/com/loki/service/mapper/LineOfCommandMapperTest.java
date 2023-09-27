package com.loki.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineOfCommandMapperTest {

    private LineOfCommandMapper lineOfCommandMapper;

    @BeforeEach
    public void setUp() {
        lineOfCommandMapper = new LineOfCommandMapperImpl();
    }
}
