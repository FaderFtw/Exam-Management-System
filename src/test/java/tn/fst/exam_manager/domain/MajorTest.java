package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.DepartmentTestSamples.*;
import static tn.fst.exam_manager.domain.MajorTestSamples.*;
import static tn.fst.exam_manager.domain.StudentClassTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class MajorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Major.class);
        Major major1 = getMajorSample1();
        Major major2 = new Major();
        assertThat(major1).isNotEqualTo(major2);

        major2.setId(major1.getId());
        assertThat(major1).isEqualTo(major2);

        major2 = getMajorSample2();
        assertThat(major1).isNotEqualTo(major2);
    }

    @Test
    void departmentTest() {
        Major major = getMajorRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        major.addDepartment(departmentBack);
        assertThat(major.getDepartments()).containsOnly(departmentBack);
        assertThat(departmentBack.getMajor()).isEqualTo(major);

        major.removeDepartment(departmentBack);
        assertThat(major.getDepartments()).doesNotContain(departmentBack);
        assertThat(departmentBack.getMajor()).isNull();

        major.departments(new HashSet<>(Set.of(departmentBack)));
        assertThat(major.getDepartments()).containsOnly(departmentBack);
        assertThat(departmentBack.getMajor()).isEqualTo(major);

        major.setDepartments(new HashSet<>());
        assertThat(major.getDepartments()).doesNotContain(departmentBack);
        assertThat(departmentBack.getMajor()).isNull();
    }

    @Test
    void studentClassTest() {
        Major major = getMajorRandomSampleGenerator();
        StudentClass studentClassBack = getStudentClassRandomSampleGenerator();

        major.setStudentClass(studentClassBack);
        assertThat(major.getStudentClass()).isEqualTo(studentClassBack);

        major.studentClass(null);
        assertThat(major.getStudentClass()).isNull();
    }
}
