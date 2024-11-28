package tn.fst.exam_manager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class TeachingSessionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeachingSessionDTO.class);
        TeachingSessionDTO teachingSessionDTO1 = new TeachingSessionDTO();
        teachingSessionDTO1.setId(1L);
        TeachingSessionDTO teachingSessionDTO2 = new TeachingSessionDTO();
        assertThat(teachingSessionDTO1).isNotEqualTo(teachingSessionDTO2);
        teachingSessionDTO2.setId(teachingSessionDTO1.getId());
        assertThat(teachingSessionDTO1).isEqualTo(teachingSessionDTO2);
        teachingSessionDTO2.setId(2L);
        assertThat(teachingSessionDTO1).isNotEqualTo(teachingSessionDTO2);
        teachingSessionDTO1.setId(null);
        assertThat(teachingSessionDTO1).isNotEqualTo(teachingSessionDTO2);
    }
}
