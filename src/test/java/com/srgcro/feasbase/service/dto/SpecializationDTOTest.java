package com.srgcro.feasbase.service.dto;

import com.srgcro.feasbase.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpecializationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecializationDTO.class);
        SpecializationDTO specializationDTO1 = new SpecializationDTO();
        specializationDTO1.setId(1L);
        SpecializationDTO specializationDTO2 = new SpecializationDTO();
        assertThat(specializationDTO1).isNotEqualTo(specializationDTO2);
        specializationDTO2.setId(specializationDTO1.getId());
        assertThat(specializationDTO1).isEqualTo(specializationDTO2);
        specializationDTO2.setId(2L);
        assertThat(specializationDTO1).isNotEqualTo(specializationDTO2);
        specializationDTO1.setId(null);
        assertThat(specializationDTO1).isNotEqualTo(specializationDTO2);
    }
}
