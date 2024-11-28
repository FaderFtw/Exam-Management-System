package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.ProfessorDetailsTestSamples.*;
import static tn.fst.exam_manager.domain.TeachingSessionTestSamples.*;
import static tn.fst.exam_manager.domain.TimetableTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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

        timetable.addProfessor(professorDetailsBack);
        assertThat(timetable.getProfessors()).containsOnly(professorDetailsBack);
        assertThat(professorDetailsBack.getTimetable()).isEqualTo(timetable);

        timetable.removeProfessor(professorDetailsBack);
        assertThat(timetable.getProfessors()).doesNotContain(professorDetailsBack);
        assertThat(professorDetailsBack.getTimetable()).isNull();

        timetable.professors(new HashSet<>(Set.of(professorDetailsBack)));
        assertThat(timetable.getProfessors()).containsOnly(professorDetailsBack);
        assertThat(professorDetailsBack.getTimetable()).isEqualTo(timetable);

        timetable.setProfessors(new HashSet<>());
        assertThat(timetable.getProfessors()).doesNotContain(professorDetailsBack);
        assertThat(professorDetailsBack.getTimetable()).isNull();
    }

    @Test
    void teachingSessionTest() {
        Timetable timetable = getTimetableRandomSampleGenerator();
        TeachingSession teachingSessionBack = getTeachingSessionRandomSampleGenerator();

        timetable.setTeachingSession(teachingSessionBack);
        assertThat(timetable.getTeachingSession()).isEqualTo(teachingSessionBack);

        timetable.teachingSession(null);
        assertThat(timetable.getTeachingSession()).isNull();
    }
}
