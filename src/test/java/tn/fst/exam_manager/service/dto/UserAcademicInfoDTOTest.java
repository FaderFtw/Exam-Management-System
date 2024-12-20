package tn.fst.exam_manager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class UserAcademicInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAcademicInfoDTO.class);
        UserAcademicInfoDTO userAcademicInfoDTO1 = new UserAcademicInfoDTO();
        userAcademicInfoDTO1.setId(1L);
        UserAcademicInfoDTO userAcademicInfoDTO2 = new UserAcademicInfoDTO();
        assertThat(userAcademicInfoDTO1).isNotEqualTo(userAcademicInfoDTO2);
        userAcademicInfoDTO2.setId(userAcademicInfoDTO1.getId());
        assertThat(userAcademicInfoDTO1).isEqualTo(userAcademicInfoDTO2);
        userAcademicInfoDTO2.setId(2L);
        assertThat(userAcademicInfoDTO1).isNotEqualTo(userAcademicInfoDTO2);
        userAcademicInfoDTO1.setId(null);
        assertThat(userAcademicInfoDTO1).isNotEqualTo(userAcademicInfoDTO2);
    }
}
