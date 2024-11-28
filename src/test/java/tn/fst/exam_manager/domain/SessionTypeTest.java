package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.ExamSessionTestSamples.*;
import static tn.fst.exam_manager.domain.SessionTypeTestSamples.*;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class SessionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionType.class);
        SessionType sessionType1 = getSessionTypeSample1();
        SessionType sessionType2 = new SessionType();
        assertThat(sessionType1).isNotEqualTo(sessionType2);

        sessionType2.setId(sessionType1.getId());
        assertThat(sessionType1).isEqualTo(sessionType2);

        sessionType2 = getSessionTypeSample2();
        assertThat(sessionType1).isNotEqualTo(sessionType2);
    }

    @Test
    void examSessionTest() {
        SessionType sessionType = getSessionTypeRandomSampleGenerator();
        ExamSession examSessionBack = getExamSessionRandomSampleGenerator();

        sessionType.setExamSession(examSessionBack);
        assertThat(sessionType.getExamSession()).isEqualTo(examSessionBack);
        assertThat(examSessionBack.getSessionType()).isEqualTo(sessionType);

        sessionType.examSession(null);
        assertThat(sessionType.getExamSession()).isNull();
        assertThat(examSessionBack.getSessionType()).isNull();
    }
}
