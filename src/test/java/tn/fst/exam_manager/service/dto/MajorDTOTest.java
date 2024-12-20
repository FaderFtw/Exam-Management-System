package tn.fst.exam_manager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class MajorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MajorDTO.class);
        MajorDTO majorDTO1 = new MajorDTO();
        majorDTO1.setId(1L);
        MajorDTO majorDTO2 = new MajorDTO();
        assertThat(majorDTO1).isNotEqualTo(majorDTO2);
        majorDTO2.setId(majorDTO1.getId());
        assertThat(majorDTO1).isEqualTo(majorDTO2);
        majorDTO2.setId(2L);
        assertThat(majorDTO1).isNotEqualTo(majorDTO2);
        majorDTO1.setId(null);
        assertThat(majorDTO1).isNotEqualTo(majorDTO2);
    }
}
