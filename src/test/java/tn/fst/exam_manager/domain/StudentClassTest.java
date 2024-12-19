package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.MajorTestSamples.*;
import static tn.fst.exam_manager.domain.StudentClassTestSamples.*;

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

        studentClass.setMajor(majorBack);
        assertThat(studentClass.getMajor()).isEqualTo(majorBack);

        studentClass.major(null);
        assertThat(studentClass.getMajor()).isNull();
    }
}
