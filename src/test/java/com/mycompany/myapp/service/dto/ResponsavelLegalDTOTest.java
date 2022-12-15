package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResponsavelLegalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResponsavelLegalDTO.class);
        ResponsavelLegalDTO responsavelLegalDTO1 = new ResponsavelLegalDTO();
        responsavelLegalDTO1.setId(1L);
        ResponsavelLegalDTO responsavelLegalDTO2 = new ResponsavelLegalDTO();
        assertThat(responsavelLegalDTO1).isNotEqualTo(responsavelLegalDTO2);
        responsavelLegalDTO2.setId(responsavelLegalDTO1.getId());
        assertThat(responsavelLegalDTO1).isEqualTo(responsavelLegalDTO2);
        responsavelLegalDTO2.setId(2L);
        assertThat(responsavelLegalDTO1).isNotEqualTo(responsavelLegalDTO2);
        responsavelLegalDTO1.setId(null);
        assertThat(responsavelLegalDTO1).isNotEqualTo(responsavelLegalDTO2);
    }
}
