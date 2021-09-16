package com.srgcro.feasbase.service.dto;

import com.srgcro.feasbase.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NosologyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NosologyDTO.class);
        NosologyDTO nosologyDTO1 = new NosologyDTO();
        nosologyDTO1.setId(1L);
        NosologyDTO nosologyDTO2 = new NosologyDTO();
        assertThat(nosologyDTO1).isNotEqualTo(nosologyDTO2);
        nosologyDTO2.setId(nosologyDTO1.getId());
        assertThat(nosologyDTO1).isEqualTo(nosologyDTO2);
        nosologyDTO2.setId(2L);
        assertThat(nosologyDTO1).isNotEqualTo(nosologyDTO2);
        nosologyDTO1.setId(null);
        assertThat(nosologyDTO1).isNotEqualTo(nosologyDTO2);
    }
}
