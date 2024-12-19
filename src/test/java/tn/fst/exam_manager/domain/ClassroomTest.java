package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.ClassroomTestSamples.*;
import static tn.fst.exam_manager.domain.DepartmentTestSamples.*;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class ClassroomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classroom.class);
        Classroom classroom1 = getClassroomSample1();
        Classroom classroom2 = new Classroom();
        assertThat(classroom1).isNotEqualTo(classroom2);

        classroom2.setId(classroom1.getId());
        assertThat(classroom1).isEqualTo(classroom2);

        classroom2 = getClassroomSample2();
        assertThat(classroom1).isNotEqualTo(classroom2);
    }

    @Test
    void departmentTest() {
        Classroom classroom = getClassroomRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        classroom.setDepartment(departmentBack);
        assertThat(classroom.getDepartment()).isEqualTo(departmentBack);

        classroom.department(null);
        assertThat(classroom.getDepartment()).isNull();
    }
}
