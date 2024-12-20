package tn.fst.exam_manager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class ClassroomDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassroomDTO.class);
        ClassroomDTO classroomDTO1 = new ClassroomDTO();
        classroomDTO1.setId(1L);
        ClassroomDTO classroomDTO2 = new ClassroomDTO();
        assertThat(classroomDTO1).isNotEqualTo(classroomDTO2);
        classroomDTO2.setId(classroomDTO1.getId());
        assertThat(classroomDTO1).isEqualTo(classroomDTO2);
        classroomDTO2.setId(2L);
        assertThat(classroomDTO1).isNotEqualTo(classroomDTO2);
        classroomDTO1.setId(null);
        assertThat(classroomDTO1).isNotEqualTo(classroomDTO2);
    }
}
