package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.ClassroomTestSamples.*;
import static tn.fst.exam_manager.domain.DepartmentTestSamples.*;
import static tn.fst.exam_manager.domain.ExamTestSamples.*;
import static tn.fst.exam_manager.domain.TeachingSessionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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

        classroom.addDepartment(departmentBack);
        assertThat(classroom.getDepartments()).containsOnly(departmentBack);
        assertThat(departmentBack.getClassroom()).isEqualTo(classroom);

        classroom.removeDepartment(departmentBack);
        assertThat(classroom.getDepartments()).doesNotContain(departmentBack);
        assertThat(departmentBack.getClassroom()).isNull();

        classroom.departments(new HashSet<>(Set.of(departmentBack)));
        assertThat(classroom.getDepartments()).containsOnly(departmentBack);
        assertThat(departmentBack.getClassroom()).isEqualTo(classroom);

        classroom.setDepartments(new HashSet<>());
        assertThat(classroom.getDepartments()).doesNotContain(departmentBack);
        assertThat(departmentBack.getClassroom()).isNull();
    }

    @Test
    void examTest() {
        Classroom classroom = getClassroomRandomSampleGenerator();
        Exam examBack = getExamRandomSampleGenerator();

        classroom.setExam(examBack);
        assertThat(classroom.getExam()).isEqualTo(examBack);

        classroom.exam(null);
        assertThat(classroom.getExam()).isNull();
    }

    @Test
    void teachingSessionTest() {
        Classroom classroom = getClassroomRandomSampleGenerator();
        TeachingSession teachingSessionBack = getTeachingSessionRandomSampleGenerator();

        classroom.setTeachingSession(teachingSessionBack);
        assertThat(classroom.getTeachingSession()).isEqualTo(teachingSessionBack);

        classroom.teachingSession(null);
        assertThat(classroom.getTeachingSession()).isNull();
    }
}
