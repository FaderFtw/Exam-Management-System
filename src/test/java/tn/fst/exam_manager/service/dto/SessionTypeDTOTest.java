package tn.fst.exam_manager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class SessionTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionTypeDTO.class);
        SessionTypeDTO sessionTypeDTO1 = new SessionTypeDTO();
        sessionTypeDTO1.setId(1L);
        SessionTypeDTO sessionTypeDTO2 = new SessionTypeDTO();
        assertThat(sessionTypeDTO1).isNotEqualTo(sessionTypeDTO2);
        sessionTypeDTO2.setId(sessionTypeDTO1.getId());
        assertThat(sessionTypeDTO1).isEqualTo(sessionTypeDTO2);
        sessionTypeDTO2.setId(2L);
        assertThat(sessionTypeDTO1).isNotEqualTo(sessionTypeDTO2);
        sessionTypeDTO1.setId(null);
        assertThat(sessionTypeDTO1).isNotEqualTo(sessionTypeDTO2);
    }
}
