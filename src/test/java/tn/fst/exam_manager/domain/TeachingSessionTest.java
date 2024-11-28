package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.fst.exam_manager.domain.ClassroomTestSamples.*;
import static tn.fst.exam_manager.domain.StudentClassTestSamples.*;
import static tn.fst.exam_manager.domain.TeachingSessionTestSamples.*;
import static tn.fst.exam_manager.domain.TimetableTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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

        teachingSession.addTimetable(timetableBack);
        assertThat(teachingSession.getTimetables()).containsOnly(timetableBack);
        assertThat(timetableBack.getTeachingSession()).isEqualTo(teachingSession);

        teachingSession.removeTimetable(timetableBack);
        assertThat(teachingSession.getTimetables()).doesNotContain(timetableBack);
        assertThat(timetableBack.getTeachingSession()).isNull();

        teachingSession.timetables(new HashSet<>(Set.of(timetableBack)));
        assertThat(teachingSession.getTimetables()).containsOnly(timetableBack);
        assertThat(timetableBack.getTeachingSession()).isEqualTo(teachingSession);

        teachingSession.setTimetables(new HashSet<>());
        assertThat(teachingSession.getTimetables()).doesNotContain(timetableBack);
        assertThat(timetableBack.getTeachingSession()).isNull();
    }

    @Test
    void studentClassTest() {
        TeachingSession teachingSession = getTeachingSessionRandomSampleGenerator();
        StudentClass studentClassBack = getStudentClassRandomSampleGenerator();

        teachingSession.addStudentClass(studentClassBack);
        assertThat(teachingSession.getStudentClasses()).containsOnly(studentClassBack);
        assertThat(studentClassBack.getTeachingSession()).isEqualTo(teachingSession);

        teachingSession.removeStudentClass(studentClassBack);
        assertThat(teachingSession.getStudentClasses()).doesNotContain(studentClassBack);
        assertThat(studentClassBack.getTeachingSession()).isNull();

        teachingSession.studentClasses(new HashSet<>(Set.of(studentClassBack)));
        assertThat(teachingSession.getStudentClasses()).containsOnly(studentClassBack);
        assertThat(studentClassBack.getTeachingSession()).isEqualTo(teachingSession);

        teachingSession.setStudentClasses(new HashSet<>());
        assertThat(teachingSession.getStudentClasses()).doesNotContain(studentClassBack);
        assertThat(studentClassBack.getTeachingSession()).isNull();
    }

    @Test
    void classroomTest() {
        TeachingSession teachingSession = getTeachingSessionRandomSampleGenerator();
        Classroom classroomBack = getClassroomRandomSampleGenerator();

        teachingSession.addClassroom(classroomBack);
        assertThat(teachingSession.getClassrooms()).containsOnly(classroomBack);
        assertThat(classroomBack.getTeachingSession()).isEqualTo(teachingSession);

        teachingSession.removeClassroom(classroomBack);
        assertThat(teachingSession.getClassrooms()).doesNotContain(classroomBack);
        assertThat(classroomBack.getTeachingSession()).isNull();

        teachingSession.classrooms(new HashSet<>(Set.of(classroomBack)));
        assertThat(teachingSession.getClassrooms()).containsOnly(classroomBack);
        assertThat(classroomBack.getTeachingSession()).isEqualTo(teachingSession);

        teachingSession.setClassrooms(new HashSet<>());
        assertThat(teachingSession.getClassrooms()).doesNotContain(classroomBack);
        assertThat(classroomBack.getTeachingSession()).isNull();
    }
}
