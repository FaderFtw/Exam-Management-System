package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.InstituteTestSamples.*;
import static tn.fst.exam_manager.domain.UserAcademicInfoTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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
    void usersTest() {
        Institute institute = getInstituteRandomSampleGenerator();
        UserAcademicInfo userAcademicInfoBack = getUserAcademicInfoRandomSampleGenerator();

        institute.addUsers(userAcademicInfoBack);
        assertThat(institute.getUsers()).containsOnly(userAcademicInfoBack);
        assertThat(userAcademicInfoBack.getInstitute()).isEqualTo(institute);

        institute.removeUsers(userAcademicInfoBack);
        assertThat(institute.getUsers()).doesNotContain(userAcademicInfoBack);
        assertThat(userAcademicInfoBack.getInstitute()).isNull();

        institute.users(new HashSet<>(Set.of(userAcademicInfoBack)));
        assertThat(institute.getUsers()).containsOnly(userAcademicInfoBack);
        assertThat(userAcademicInfoBack.getInstitute()).isEqualTo(institute);

        institute.setUsers(new HashSet<>());
        assertThat(institute.getUsers()).doesNotContain(userAcademicInfoBack);
        assertThat(userAcademicInfoBack.getInstitute()).isNull();
    }
}
