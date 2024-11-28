package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.ClassroomTestSamples.*;
import static tn.fst.exam_manager.domain.DepartmentTestSamples.*;
import static tn.fst.exam_manager.domain.ExamSessionTestSamples.*;
import static tn.fst.exam_manager.domain.InstituteTestSamples.*;
import static tn.fst.exam_manager.domain.MajorTestSamples.*;
import static tn.fst.exam_manager.domain.ReportTestSamples.*;

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

        department.addInstitute(instituteBack);
        assertThat(department.getInstitutes()).containsOnly(instituteBack);
        assertThat(instituteBack.getDepartment()).isEqualTo(department);

        department.removeInstitute(instituteBack);
        assertThat(department.getInstitutes()).doesNotContain(instituteBack);
        assertThat(instituteBack.getDepartment()).isNull();

        department.institutes(new HashSet<>(Set.of(instituteBack)));
        assertThat(department.getInstitutes()).containsOnly(instituteBack);
        assertThat(instituteBack.getDepartment()).isEqualTo(department);

        department.setInstitutes(new HashSet<>());
        assertThat(department.getInstitutes()).doesNotContain(instituteBack);
        assertThat(instituteBack.getDepartment()).isNull();
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
    void classroomTest() {
        Department department = getDepartmentRandomSampleGenerator();
        Classroom classroomBack = getClassroomRandomSampleGenerator();

        department.setClassroom(classroomBack);
        assertThat(department.getClassroom()).isEqualTo(classroomBack);

        department.classroom(null);
        assertThat(department.getClassroom()).isNull();
    }

    @Test
    void majorTest() {
        Department department = getDepartmentRandomSampleGenerator();
        Major majorBack = getMajorRandomSampleGenerator();

        department.setMajor(majorBack);
        assertThat(department.getMajor()).isEqualTo(majorBack);

        department.major(null);
        assertThat(department.getMajor()).isNull();
    }

    @Test
    void reportTest() {
        Department department = getDepartmentRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        department.setReport(reportBack);
        assertThat(department.getReport()).isEqualTo(reportBack);

        department.report(null);
        assertThat(department.getReport()).isNull();
    }
}
