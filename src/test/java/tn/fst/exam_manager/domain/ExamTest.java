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

        exam.addClassroom(classroomBack);
        assertThat(exam.getClassrooms()).containsOnly(classroomBack);
        assertThat(classroomBack.getExam()).isEqualTo(exam);

        exam.removeClassroom(classroomBack);
        assertThat(exam.getClassrooms()).doesNotContain(classroomBack);
        assertThat(classroomBack.getExam()).isNull();

        exam.classrooms(new HashSet<>(Set.of(classroomBack)));
        assertThat(exam.getClassrooms()).containsOnly(classroomBack);
        assertThat(classroomBack.getExam()).isEqualTo(exam);

        exam.setClassrooms(new HashSet<>());
        assertThat(exam.getClassrooms()).doesNotContain(classroomBack);
        assertThat(classroomBack.getExam()).isNull();
    }

    @Test
    void studentClassTest() {
        Exam exam = getExamRandomSampleGenerator();
        StudentClass studentClassBack = getStudentClassRandomSampleGenerator();

        exam.addStudentClass(studentClassBack);
        assertThat(exam.getStudentClasses()).containsOnly(studentClassBack);
        assertThat(studentClassBack.getExam()).isEqualTo(exam);

        exam.removeStudentClass(studentClassBack);
        assertThat(exam.getStudentClasses()).doesNotContain(studentClassBack);
        assertThat(studentClassBack.getExam()).isNull();

        exam.studentClasses(new HashSet<>(Set.of(studentClassBack)));
        assertThat(exam.getStudentClasses()).containsOnly(studentClassBack);
        assertThat(studentClassBack.getExam()).isEqualTo(exam);

        exam.setStudentClasses(new HashSet<>());
        assertThat(exam.getStudentClasses()).doesNotContain(studentClassBack);
        assertThat(studentClassBack.getExam()).isNull();
    }

    @Test
    void sessionTest() {
        Exam exam = getExamRandomSampleGenerator();
        ExamSession examSessionBack = getExamSessionRandomSampleGenerator();

        exam.addSession(examSessionBack);
        assertThat(exam.getSessions()).containsOnly(examSessionBack);
        assertThat(examSessionBack.getExam()).isEqualTo(exam);

        exam.removeSession(examSessionBack);
        assertThat(exam.getSessions()).doesNotContain(examSessionBack);
        assertThat(examSessionBack.getExam()).isNull();

        exam.sessions(new HashSet<>(Set.of(examSessionBack)));
        assertThat(exam.getSessions()).containsOnly(examSessionBack);
        assertThat(examSessionBack.getExam()).isEqualTo(exam);

        exam.setSessions(new HashSet<>());
        assertThat(exam.getSessions()).doesNotContain(examSessionBack);
        assertThat(examSessionBack.getExam()).isNull();
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
