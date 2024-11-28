package tn.fst.exam_manager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class ReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDTO.class);
        ReportDTO reportDTO1 = new ReportDTO();
        reportDTO1.setId(1L);
        ReportDTO reportDTO2 = new ReportDTO();
        assertThat(reportDTO1).isNotEqualTo(reportDTO2);
        reportDTO2.setId(reportDTO1.getId());
        assertThat(reportDTO1).isEqualTo(reportDTO2);
        reportDTO2.setId(2L);
        assertThat(reportDTO1).isNotEqualTo(reportDTO2);
        reportDTO1.setId(null);
        assertThat(reportDTO1).isNotEqualTo(reportDTO2);
    }
}
