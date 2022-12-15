package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResponsavelLegalMapperTest {

    private ResponsavelLegalMapper responsavelLegalMapper;

    @BeforeEach
    public void setUp() {
        responsavelLegalMapper = new ResponsavelLegalMapperImpl();
    }
}
