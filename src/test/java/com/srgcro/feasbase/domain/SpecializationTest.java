package com.srgcro.feasbase.domain;

import com.srgcro.feasbase.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpecializationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Specialization.class);
        Specialization specialization1 = new Specialization();
        specialization1.setId(1L);
        Specialization specialization2 = new Specialization();
        specialization2.setId(specialization1.getId());
        assertThat(specialization1).isEqualTo(specialization2);
        specialization2.setId(2L);
        assertThat(specialization1).isNotEqualTo(specialization2);
        specialization1.setId(null);
        assertThat(specialization1).isNotEqualTo(specialization2);
    }
}
