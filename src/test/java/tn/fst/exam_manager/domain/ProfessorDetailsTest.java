package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.ExamTestSamples.*;
import static tn.fst.exam_manager.domain.ProfessorDetailsTestSamples.*;
import static tn.fst.exam_manager.domain.ReportTestSamples.*;
import static tn.fst.exam_manager.domain.TimetableTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class ProfessorDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessorDetails.class);
        ProfessorDetails professorDetails1 = getProfessorDetailsSample1();
        ProfessorDetails professorDetails2 = new ProfessorDetails();
        assertThat(professorDetails1).isNotEqualTo(professorDetails2);

        professorDetails2.setId(professorDetails1.getId());
        assertThat(professorDetails1).isEqualTo(professorDetails2);

        professorDetails2 = getProfessorDetailsSample2();
        assertThat(professorDetails1).isNotEqualTo(professorDetails2);
    }

    @Test
    void supervisedExamsTest() {
        ProfessorDetails professorDetails = getProfessorDetailsRandomSampleGenerator();
        Exam examBack = getExamRandomSampleGenerator();

        professorDetails.addSupervisedExams(examBack);
        assertThat(professorDetails.getSupervisedExams()).containsOnly(examBack);
        assertThat(examBack.getSupervisors()).containsOnly(professorDetails);

        professorDetails.removeSupervisedExams(examBack);
        assertThat(professorDetails.getSupervisedExams()).doesNotContain(examBack);
        assertThat(examBack.getSupervisors()).doesNotContain(professorDetails);

        professorDetails.supervisedExams(new HashSet<>(Set.of(examBack)));
        assertThat(professorDetails.getSupervisedExams()).containsOnly(examBack);
        assertThat(examBack.getSupervisors()).containsOnly(professorDetails);

        professorDetails.setSupervisedExams(new HashSet<>());
        assertThat(professorDetails.getSupervisedExams()).doesNotContain(examBack);
        assertThat(examBack.getSupervisors()).doesNotContain(professorDetails);
    }

    @Test
    void reportTest() {
        ProfessorDetails professorDetails = getProfessorDetailsRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        professorDetails.setReport(reportBack);
        assertThat(professorDetails.getReport()).isEqualTo(reportBack);

        professorDetails.report(null);
        assertThat(professorDetails.getReport()).isNull();
    }

    @Test
    void timetableTest() {
        ProfessorDetails professorDetails = getProfessorDetailsRandomSampleGenerator();
        Timetable timetableBack = getTimetableRandomSampleGenerator();

        professorDetails.setTimetable(timetableBack);
        assertThat(professorDetails.getTimetable()).isEqualTo(timetableBack);

        professorDetails.timetable(null);
        assertThat(professorDetails.getTimetable()).isNull();
    }
}
