package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.DepartmentTestSamples.*;
import static tn.fst.exam_manager.domain.ExamSessionTestSamples.*;
import static tn.fst.exam_manager.domain.ExamTestSamples.*;
import static tn.fst.exam_manager.domain.ReportTestSamples.*;
import static tn.fst.exam_manager.domain.SessionTypeTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class ExamSessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamSession.class);
        ExamSession examSession1 = getExamSessionSample1();
        ExamSession examSession2 = new ExamSession();
        assertThat(examSession1).isNotEqualTo(examSession2);

        examSession2.setId(examSession1.getId());
        assertThat(examSession1).isEqualTo(examSession2);

        examSession2 = getExamSessionSample2();
        assertThat(examSession1).isNotEqualTo(examSession2);
    }

    @Test
    void sessionTypeTest() {
        ExamSession examSession = getExamSessionRandomSampleGenerator();
        SessionType sessionTypeBack = getSessionTypeRandomSampleGenerator();

        examSession.setSessionType(sessionTypeBack);
        assertThat(examSession.getSessionType()).isEqualTo(sessionTypeBack);

        examSession.sessionType(null);
        assertThat(examSession.getSessionType()).isNull();
    }

    @Test
    void departmentsTest() {
        ExamSession examSession = getExamSessionRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        examSession.addDepartments(departmentBack);
        assertThat(examSession.getDepartments()).containsOnly(departmentBack);

        examSession.removeDepartments(departmentBack);
        assertThat(examSession.getDepartments()).doesNotContain(departmentBack);

        examSession.departments(new HashSet<>(Set.of(departmentBack)));
        assertThat(examSession.getDepartments()).containsOnly(departmentBack);

        examSession.setDepartments(new HashSet<>());
        assertThat(examSession.getDepartments()).doesNotContain(departmentBack);
    }

    @Test
    void examTest() {
        ExamSession examSession = getExamSessionRandomSampleGenerator();
        Exam examBack = getExamRandomSampleGenerator();

        examSession.setExam(examBack);
        assertThat(examSession.getExam()).isEqualTo(examBack);

        examSession.exam(null);
        assertThat(examSession.getExam()).isNull();
    }

    @Test
    void reportTest() {
        ExamSession examSession = getExamSessionRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        examSession.setReport(reportBack);
        assertThat(examSession.getReport()).isEqualTo(reportBack);

        examSession.report(null);
        assertThat(examSession.getReport()).isNull();
    }
}
