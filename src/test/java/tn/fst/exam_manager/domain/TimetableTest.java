package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.ProfessorDetailsTestSamples.*;
import static tn.fst.exam_manager.domain.TimetableTestSamples.*;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class TimetableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Timetable.class);
        Timetable timetable1 = getTimetableSample1();
        Timetable timetable2 = new Timetable();
        assertThat(timetable1).isNotEqualTo(timetable2);

        timetable2.setId(timetable1.getId());
        assertThat(timetable1).isEqualTo(timetable2);

        timetable2 = getTimetableSample2();
        assertThat(timetable1).isNotEqualTo(timetable2);
    }

    @Test
    void professorTest() {
        Timetable timetable = getTimetableRandomSampleGenerator();
        ProfessorDetails professorDetailsBack = getProfessorDetailsRandomSampleGenerator();

        timetable.setProfessor(professorDetailsBack);
        assertThat(timetable.getProfessor()).isEqualTo(professorDetailsBack);

        timetable.professor(null);
        assertThat(timetable.getProfessor()).isNull();
    }
}
