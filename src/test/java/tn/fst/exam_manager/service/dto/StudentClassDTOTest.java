package tn.fst.exam_manager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class StudentClassDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentClassDTO.class);
        StudentClassDTO studentClassDTO1 = new StudentClassDTO();
        studentClassDTO1.setId(1L);
        StudentClassDTO studentClassDTO2 = new StudentClassDTO();
        assertThat(studentClassDTO1).isNotEqualTo(studentClassDTO2);
        studentClassDTO2.setId(studentClassDTO1.getId());
        assertThat(studentClassDTO1).isEqualTo(studentClassDTO2);
        studentClassDTO2.setId(2L);
        assertThat(studentClassDTO1).isNotEqualTo(studentClassDTO2);
        studentClassDTO1.setId(null);
        assertThat(studentClassDTO1).isNotEqualTo(studentClassDTO2);
    }
}
