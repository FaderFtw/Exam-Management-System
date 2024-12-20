package tn.fst.exam_manager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class ProfessorDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessorDetailsDTO.class);
        ProfessorDetailsDTO professorDetailsDTO1 = new ProfessorDetailsDTO();
        professorDetailsDTO1.setId(1L);
        ProfessorDetailsDTO professorDetailsDTO2 = new ProfessorDetailsDTO();
        assertThat(professorDetailsDTO1).isNotEqualTo(professorDetailsDTO2);
        professorDetailsDTO2.setId(professorDetailsDTO1.getId());
        assertThat(professorDetailsDTO1).isEqualTo(professorDetailsDTO2);
        professorDetailsDTO2.setId(2L);
        assertThat(professorDetailsDTO1).isNotEqualTo(professorDetailsDTO2);
        professorDetailsDTO1.setId(null);
        assertThat(professorDetailsDTO1).isNotEqualTo(professorDetailsDTO2);
    }
}
