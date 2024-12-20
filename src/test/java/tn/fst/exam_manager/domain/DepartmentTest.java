package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.DepartmentTestSamples.*;
import static tn.fst.exam_manager.domain.ExamSessionTestSamples.*;
import static tn.fst.exam_manager.domain.InstituteTestSamples.*;
import static tn.fst.exam_manager.domain.UserAcademicInfoTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class DepartmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Department.class);
        Department department1 = getDepartmentSample1();
        Department department2 = new Department();
        assertThat(department1).isNotEqualTo(department2);

        department2.setId(department1.getId());
        assertThat(department1).isEqualTo(department2);

        department2 = getDepartmentSample2();
        assertThat(department1).isNotEqualTo(department2);
    }

    @Test
    void instituteTest() {
        Department department = getDepartmentRandomSampleGenerator();
        Institute instituteBack = getInstituteRandomSampleGenerator();

        department.setInstitute(instituteBack);
        assertThat(department.getInstitute()).isEqualTo(instituteBack);

        department.institute(null);
        assertThat(department.getInstitute()).isNull();
    }

    @Test
    void examSessionsTest() {
        Department department = getDepartmentRandomSampleGenerator();
        ExamSession examSessionBack = getExamSessionRandomSampleGenerator();

        department.addExamSessions(examSessionBack);
        assertThat(department.getExamSessions()).containsOnly(examSessionBack);
        assertThat(examSessionBack.getDepartments()).containsOnly(department);

        department.removeExamSessions(examSessionBack);
        assertThat(department.getExamSessions()).doesNotContain(examSessionBack);
        assertThat(examSessionBack.getDepartments()).doesNotContain(department);

        department.examSessions(new HashSet<>(Set.of(examSessionBack)));
        assertThat(department.getExamSessions()).containsOnly(examSessionBack);
        assertThat(examSessionBack.getDepartments()).containsOnly(department);

        department.setExamSessions(new HashSet<>());
        assertThat(department.getExamSessions()).doesNotContain(examSessionBack);
        assertThat(examSessionBack.getDepartments()).doesNotContain(department);
    }

    @Test
    void usersTest() {
        Department department = getDepartmentRandomSampleGenerator();
        UserAcademicInfo userAcademicInfoBack = getUserAcademicInfoRandomSampleGenerator();

        department.addUsers(userAcademicInfoBack);
        assertThat(department.getUsers()).containsOnly(userAcademicInfoBack);
        assertThat(userAcademicInfoBack.getDepartment()).isEqualTo(department);

        department.removeUsers(userAcademicInfoBack);
        assertThat(department.getUsers()).doesNotContain(userAcademicInfoBack);
        assertThat(userAcademicInfoBack.getDepartment()).isNull();

        department.users(new HashSet<>(Set.of(userAcademicInfoBack)));
        assertThat(department.getUsers()).containsOnly(userAcademicInfoBack);
        assertThat(userAcademicInfoBack.getDepartment()).isEqualTo(department);

        department.setUsers(new HashSet<>());
        assertThat(department.getUsers()).doesNotContain(userAcademicInfoBack);
        assertThat(userAcademicInfoBack.getDepartment()).isNull();
    }
}
