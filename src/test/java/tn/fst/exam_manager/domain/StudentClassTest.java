package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.ExamTestSamples.*;
import static tn.fst.exam_manager.domain.MajorTestSamples.*;
import static tn.fst.exam_manager.domain.StudentClassTestSamples.*;
import static tn.fst.exam_manager.domain.TeachingSessionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class StudentClassTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentClass.class);
        StudentClass studentClass1 = getStudentClassSample1();
        StudentClass studentClass2 = new StudentClass();
        assertThat(studentClass1).isNotEqualTo(studentClass2);

        studentClass2.setId(studentClass1.getId());
        assertThat(studentClass1).isEqualTo(studentClass2);

        studentClass2 = getStudentClassSample2();
        assertThat(studentClass1).isNotEqualTo(studentClass2);
    }

    @Test
    void majorTest() {
        StudentClass studentClass = getStudentClassRandomSampleGenerator();
        Major majorBack = getMajorRandomSampleGenerator();

        studentClass.addMajor(majorBack);
        assertThat(studentClass.getMajors()).containsOnly(majorBack);
        assertThat(majorBack.getStudentClass()).isEqualTo(studentClass);

        studentClass.removeMajor(majorBack);
        assertThat(studentClass.getMajors()).doesNotContain(majorBack);
        assertThat(majorBack.getStudentClass()).isNull();

        studentClass.majors(new HashSet<>(Set.of(majorBack)));
        assertThat(studentClass.getMajors()).containsOnly(majorBack);
        assertThat(majorBack.getStudentClass()).isEqualTo(studentClass);

        studentClass.setMajors(new HashSet<>());
        assertThat(studentClass.getMajors()).doesNotContain(majorBack);
        assertThat(majorBack.getStudentClass()).isNull();
    }

    @Test
    void examTest() {
        StudentClass studentClass = getStudentClassRandomSampleGenerator();
        Exam examBack = getExamRandomSampleGenerator();

        studentClass.setExam(examBack);
        assertThat(studentClass.getExam()).isEqualTo(examBack);

        studentClass.exam(null);
        assertThat(studentClass.getExam()).isNull();
    }

    @Test
    void teachingSessionTest() {
        StudentClass studentClass = getStudentClassRandomSampleGenerator();
        TeachingSession teachingSessionBack = getTeachingSessionRandomSampleGenerator();

        studentClass.setTeachingSession(teachingSessionBack);
        assertThat(studentClass.getTeachingSession()).isEqualTo(teachingSessionBack);

        studentClass.teachingSession(null);
        assertThat(studentClass.getTeachingSession()).isNull();
    }
}
