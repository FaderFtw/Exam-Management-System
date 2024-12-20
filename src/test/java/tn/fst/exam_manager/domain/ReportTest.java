package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.DepartmentTestSamples.*;
import static tn.fst.exam_manager.domain.ExamSessionTestSamples.*;
import static tn.fst.exam_manager.domain.InstituteTestSamples.*;
import static tn.fst.exam_manager.domain.ProfessorDetailsTestSamples.*;
import static tn.fst.exam_manager.domain.ReportTestSamples.*;

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

        report.setProfessor(professorDetailsBack);
        assertThat(report.getProfessor()).isEqualTo(professorDetailsBack);

        report.professor(null);
        assertThat(report.getProfessor()).isNull();
    }

    @Test
    void examSessionTest() {
        Report report = getReportRandomSampleGenerator();
        ExamSession examSessionBack = getExamSessionRandomSampleGenerator();

        report.setExamSession(examSessionBack);
        assertThat(report.getExamSession()).isEqualTo(examSessionBack);

        report.examSession(null);
        assertThat(report.getExamSession()).isNull();
    }

    @Test
    void departmentTest() {
        Report report = getReportRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        report.setDepartment(departmentBack);
        assertThat(report.getDepartment()).isEqualTo(departmentBack);

        report.department(null);
        assertThat(report.getDepartment()).isNull();
    }

    @Test
    void instituteTest() {
        Report report = getReportRandomSampleGenerator();
        Institute instituteBack = getInstituteRandomSampleGenerator();

        report.setInstitute(instituteBack);
        assertThat(report.getInstitute()).isEqualTo(instituteBack);

        report.institute(null);
        assertThat(report.getInstitute()).isNull();
    }
}
