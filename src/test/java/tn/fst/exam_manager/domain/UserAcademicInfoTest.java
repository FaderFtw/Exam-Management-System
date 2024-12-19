package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.DepartmentTestSamples.*;
import static tn.fst.exam_manager.domain.InstituteTestSamples.*;
import static tn.fst.exam_manager.domain.UserAcademicInfoTestSamples.*;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class UserAcademicInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAcademicInfo.class);
        UserAcademicInfo userAcademicInfo1 = getUserAcademicInfoSample1();
        UserAcademicInfo userAcademicInfo2 = new UserAcademicInfo();
        assertThat(userAcademicInfo1).isNotEqualTo(userAcademicInfo2);

        userAcademicInfo2.setId(userAcademicInfo1.getId());
        assertThat(userAcademicInfo1).isEqualTo(userAcademicInfo2);

        userAcademicInfo2 = getUserAcademicInfoSample2();
        assertThat(userAcademicInfo1).isNotEqualTo(userAcademicInfo2);
    }

    @Test
    void departmentTest() {
        UserAcademicInfo userAcademicInfo = getUserAcademicInfoRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        userAcademicInfo.setDepartment(departmentBack);
        assertThat(userAcademicInfo.getDepartment()).isEqualTo(departmentBack);

        userAcademicInfo.department(null);
        assertThat(userAcademicInfo.getDepartment()).isNull();
    }

    @Test
    void instituteTest() {
        UserAcademicInfo userAcademicInfo = getUserAcademicInfoRandomSampleGenerator();
        Institute instituteBack = getInstituteRandomSampleGenerator();

        userAcademicInfo.setInstitute(instituteBack);
        assertThat(userAcademicInfo.getInstitute()).isEqualTo(instituteBack);

        userAcademicInfo.institute(null);
        assertThat(userAcademicInfo.getInstitute()).isNull();
    }
}
