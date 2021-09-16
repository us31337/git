package com.srgcro.feasbase.domain;

import com.srgcro.feasbase.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NosologyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nosology.class);
        Nosology nosology1 = new Nosology();
        nosology1.setId(1L);
        Nosology nosology2 = new Nosology();
        nosology2.setId(nosology1.getId());
        assertThat(nosology1).isEqualTo(nosology2);
        nosology2.setId(2L);
        assertThat(nosology1).isNotEqualTo(nosology2);
        nosology1.setId(null);
        assertThat(nosology1).isNotEqualTo(nosology2);
    }
}
