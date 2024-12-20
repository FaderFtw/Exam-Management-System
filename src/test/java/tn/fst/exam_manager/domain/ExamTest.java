package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.ClassroomTestSamples.*;
import static tn.fst.exam_manager.domain.ExamSessionTestSamples.*;
import static tn.fst.exam_manager.domain.ExamTestSamples.*;
import static tn.fst.exam_manager.domain.ProfessorDetailsTestSamples.*;
import static tn.fst.exam_manager.domain.StudentClassTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class ExamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Exam.class);
        Exam exam1 = getExamSample1();
        Exam exam2 = new Exam();
        assertThat(exam1).isNotEqualTo(exam2);

        exam2.setId(exam1.getId());
        assertThat(exam1).isEqualTo(exam2);

        exam2 = getExamSample2();
        assertThat(exam1).isNotEqualTo(exam2);
    }

    @Test
    void classroomTest() {
        Exam exam = getExamRandomSampleGenerator();
        Classroom classroomBack = getClassroomRandomSampleGenerator();

        exam.setClassroom(classroomBack);
        assertThat(exam.getClassroom()).isEqualTo(classroomBack);

        exam.classroom(null);
        assertThat(exam.getClassroom()).isNull();
    }

    @Test
    void studentClassTest() {
        Exam exam = getExamRandomSampleGenerator();
        StudentClass studentClassBack = getStudentClassRandomSampleGenerator();

        exam.setStudentClass(studentClassBack);
        assertThat(exam.getStudentClass()).isEqualTo(studentClassBack);

        exam.studentClass(null);
        assertThat(exam.getStudentClass()).isNull();
    }

    @Test
    void sessionTest() {
        Exam exam = getExamRandomSampleGenerator();
        ExamSession examSessionBack = getExamSessionRandomSampleGenerator();

        exam.setSession(examSessionBack);
        assertThat(exam.getSession()).isEqualTo(examSessionBack);

        exam.session(null);
        assertThat(exam.getSession()).isNull();
    }

    @Test
    void supervisorsTest() {
        Exam exam = getExamRandomSampleGenerator();
        ProfessorDetails professorDetailsBack = getProfessorDetailsRandomSampleGenerator();

        exam.addSupervisors(professorDetailsBack);
        assertThat(exam.getSupervisors()).containsOnly(professorDetailsBack);

        exam.removeSupervisors(professorDetailsBack);
        assertThat(exam.getSupervisors()).doesNotContain(professorDetailsBack);

        exam.supervisors(new HashSet<>(Set.of(professorDetailsBack)));
        assertThat(exam.getSupervisors()).containsOnly(professorDetailsBack);

        exam.setSupervisors(new HashSet<>());
        assertThat(exam.getSupervisors()).doesNotContain(professorDetailsBack);
    }
}
