package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.ClassroomTestSamples.*;
import static tn.fst.exam_manager.domain.StudentClassTestSamples.*;
import static tn.fst.exam_manager.domain.TeachingSessionTestSamples.*;
import static tn.fst.exam_manager.domain.TimetableTestSamples.*;

import org.junit.jupiter.api.Test;
import tn.fst.exam_manager.web.rest.TestUtil;

class TeachingSessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeachingSession.class);
        TeachingSession teachingSession1 = getTeachingSessionSample1();
        TeachingSession teachingSession2 = new TeachingSession();
        assertThat(teachingSession1).isNotEqualTo(teachingSession2);

        teachingSession2.setId(teachingSession1.getId());
        assertThat(teachingSession1).isEqualTo(teachingSession2);

        teachingSession2 = getTeachingSessionSample2();
        assertThat(teachingSession1).isNotEqualTo(teachingSession2);
    }

    @Test
    void timetableTest() {
        TeachingSession teachingSession = getTeachingSessionRandomSampleGenerator();
        Timetable timetableBack = getTimetableRandomSampleGenerator();

        teachingSession.setTimetable(timetableBack);
        assertThat(teachingSession.getTimetable()).isEqualTo(timetableBack);

        teachingSession.timetable(null);
        assertThat(teachingSession.getTimetable()).isNull();
    }

    @Test
    void studentClassTest() {
        TeachingSession teachingSession = getTeachingSessionRandomSampleGenerator();
        StudentClass studentClassBack = getStudentClassRandomSampleGenerator();

        teachingSession.setStudentClass(studentClassBack);
        assertThat(teachingSession.getStudentClass()).isEqualTo(studentClassBack);

        teachingSession.studentClass(null);
        assertThat(teachingSession.getStudentClass()).isNull();
    }

    @Test
    void classroomTest() {
        TeachingSession teachingSession = getTeachingSessionRandomSampleGenerator();
        Classroom classroomBack = getClassroomRandomSampleGenerator();

        teachingSession.setClassroom(classroomBack);
        assertThat(teachingSession.getClassroom()).isEqualTo(classroomBack);

        teachingSession.classroom(null);
        assertThat(teachingSession.getClassroom()).isNull();
    }
}
