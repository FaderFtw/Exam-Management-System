package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.DepartmentTestSamples.*;
import static tn.fst.exam_manager.domain.ExamSessionTestSamples.*;
import static tn.fst.exam_manager.domain.InstituteTestSamples.*;
import static tn.fst.exam_manager.domain.ProfessorDetailsTestSamples.*;
import static tn.fst.exam_manager.domain.ReportTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class ReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Report.class);
        Report report1 = getReportSample1();
        Report report2 = new Report();
        assertThat(report1).isNotEqualTo(report2);

        report2.setId(report1.getId());
        assertThat(report1).isEqualTo(report2);

        report2 = getReportSample2();
        assertThat(report1).isNotEqualTo(report2);
    }

    @Test
    void professorTest() {
        Report report = getReportRandomSampleGenerator();
        ProfessorDetails professorDetailsBack = getProfessorDetailsRandomSampleGenerator();

        report.addProfessor(professorDetailsBack);
        assertThat(report.getProfessors()).containsOnly(professorDetailsBack);
        assertThat(professorDetailsBack.getReport()).isEqualTo(report);

        report.removeProfessor(professorDetailsBack);
        assertThat(report.getProfessors()).doesNotContain(professorDetailsBack);
        assertThat(professorDetailsBack.getReport()).isNull();

        report.professors(new HashSet<>(Set.of(professorDetailsBack)));
        assertThat(report.getProfessors()).containsOnly(professorDetailsBack);
        assertThat(professorDetailsBack.getReport()).isEqualTo(report);

        report.setProfessors(new HashSet<>());
        assertThat(report.getProfessors()).doesNotContain(professorDetailsBack);
        assertThat(professorDetailsBack.getReport()).isNull();
    }

    @Test
    void examSessionTest() {
        Report report = getReportRandomSampleGenerator();
        ExamSession examSessionBack = getExamSessionRandomSampleGenerator();

        report.addExamSession(examSessionBack);
        assertThat(report.getExamSessions()).containsOnly(examSessionBack);
        assertThat(examSessionBack.getReport()).isEqualTo(report);

        report.removeExamSession(examSessionBack);
        assertThat(report.getExamSessions()).doesNotContain(examSessionBack);
        assertThat(examSessionBack.getReport()).isNull();

        report.examSessions(new HashSet<>(Set.of(examSessionBack)));
        assertThat(report.getExamSessions()).containsOnly(examSessionBack);
        assertThat(examSessionBack.getReport()).isEqualTo(report);

        report.setExamSessions(new HashSet<>());
        assertThat(report.getExamSessions()).doesNotContain(examSessionBack);
        assertThat(examSessionBack.getReport()).isNull();
    }

    @Test
    void departmentTest() {
        Report report = getReportRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        report.addDepartment(departmentBack);
        assertThat(report.getDepartments()).containsOnly(departmentBack);
        assertThat(departmentBack.getReport()).isEqualTo(report);

        report.removeDepartment(departmentBack);
        assertThat(report.getDepartments()).doesNotContain(departmentBack);
        assertThat(departmentBack.getReport()).isNull();

        report.departments(new HashSet<>(Set.of(departmentBack)));
        assertThat(report.getDepartments()).containsOnly(departmentBack);
        assertThat(departmentBack.getReport()).isEqualTo(report);

        report.setDepartments(new HashSet<>());
        assertThat(report.getDepartments()).doesNotContain(departmentBack);
        assertThat(departmentBack.getReport()).isNull();
    }

    @Test
    void instituteTest() {
        Report report = getReportRandomSampleGenerator();
        Institute instituteBack = getInstituteRandomSampleGenerator();

        report.addInstitute(instituteBack);
        assertThat(report.getInstitutes()).containsOnly(instituteBack);
        assertThat(instituteBack.getReport()).isEqualTo(report);

        report.removeInstitute(instituteBack);
        assertThat(report.getInstitutes()).doesNotContain(instituteBack);
        assertThat(instituteBack.getReport()).isNull();

        report.institutes(new HashSet<>(Set.of(instituteBack)));
        assertThat(report.getInstitutes()).containsOnly(instituteBack);
        assertThat(instituteBack.getReport()).isEqualTo(report);

        report.setInstitutes(new HashSet<>());
        assertThat(report.getInstitutes()).doesNotContain(instituteBack);
        assertThat(instituteBack.getReport()).isNull();
    }
}
