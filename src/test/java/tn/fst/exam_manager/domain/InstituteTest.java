package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.DepartmentTestSamples.*;
import static tn.fst.exam_manager.domain.InstituteTestSamples.*;
import static tn.fst.exam_manager.domain.ReportTestSamples.*;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class InstituteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Institute.class);
        Institute institute1 = getInstituteSample1();
        Institute institute2 = new Institute();
        assertThat(institute1).isNotEqualTo(institute2);

        institute2.setId(institute1.getId());
        assertThat(institute1).isEqualTo(institute2);

        institute2 = getInstituteSample2();
        assertThat(institute1).isNotEqualTo(institute2);
    }

    @Test
    void departmentTest() {
        Institute institute = getInstituteRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        institute.setDepartment(departmentBack);
        assertThat(institute.getDepartment()).isEqualTo(departmentBack);

        institute.department(null);
        assertThat(institute.getDepartment()).isNull();
    }

    @Test
    void reportTest() {
        Institute institute = getInstituteRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        institute.setReport(reportBack);
        assertThat(institute.getReport()).isEqualTo(reportBack);

        institute.report(null);
        assertThat(institute.getReport()).isNull();
    }
}
