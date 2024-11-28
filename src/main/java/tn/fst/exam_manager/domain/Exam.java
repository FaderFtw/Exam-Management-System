package tn.fst.exam_manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Exam.
 */
@Entity
@Table(name = "exam")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exam")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "departments", "exam", "teachingSession" }, allowSetters = true)
    private Set<Classroom> classrooms = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exam")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "majors", "exam", "teachingSession" }, allowSetters = true)
    private Set<StudentClass> studentClasses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exam")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sessionType", "departments", "exam", "report" }, allowSetters = true)
    private Set<ExamSession> sessions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_exam__supervisors",
        joinColumns = @JoinColumn(name = "exam_id"),
        inverseJoinColumns = @JoinColumn(name = "supervisors_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "supervisedExams", "report", "timetable" }, allowSetters = true)
    private Set<ProfessorDetails> supervisors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Exam id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Exam name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Classroom> getClassrooms() {
        return this.classrooms;
    }

    public void setClassrooms(Set<Classroom> classrooms) {
        if (this.classrooms != null) {
            this.classrooms.forEach(i -> i.setExam(null));
        }
        if (classrooms != null) {
            classrooms.forEach(i -> i.setExam(this));
        }
        this.classrooms = classrooms;
    }

    public Exam classrooms(Set<Classroom> classrooms) {
        this.setClassrooms(classrooms);
        return this;
    }

    public Exam addClassroom(Classroom classroom) {
        this.classrooms.add(classroom);
        classroom.setExam(this);
        return this;
    }

    public Exam removeClassroom(Classroom classroom) {
        this.classrooms.remove(classroom);
        classroom.setExam(null);
        return this;
    }

    public Set<StudentClass> getStudentClasses() {
        return this.studentClasses;
    }

    public void setStudentClasses(Set<StudentClass> studentClasses) {
        if (this.studentClasses != null) {
            this.studentClasses.forEach(i -> i.setExam(null));
        }
        if (studentClasses != null) {
            studentClasses.forEach(i -> i.setExam(this));
        }
        this.studentClasses = studentClasses;
    }

    public Exam studentClasses(Set<StudentClass> studentClasses) {
        this.setStudentClasses(studentClasses);
        return this;
    }

    public Exam addStudentClass(StudentClass studentClass) {
        this.studentClasses.add(studentClass);
        studentClass.setExam(this);
        return this;
    }

    public Exam removeStudentClass(StudentClass studentClass) {
        this.studentClasses.remove(studentClass);
        studentClass.setExam(null);
        return this;
    }

    public Set<ExamSession> getSessions() {
        return this.sessions;
    }

    public void setSessions(Set<ExamSession> examSessions) {
        if (this.sessions != null) {
            this.sessions.forEach(i -> i.setExam(null));
        }
        if (examSessions != null) {
            examSessions.forEach(i -> i.setExam(this));
        }
        this.sessions = examSessions;
    }

    public Exam sessions(Set<ExamSession> examSessions) {
        this.setSessions(examSessions);
        return this;
    }

    public Exam addSession(ExamSession examSession) {
        this.sessions.add(examSession);
        examSession.setExam(this);
        return this;
    }

    public Exam removeSession(ExamSession examSession) {
        this.sessions.remove(examSession);
        examSession.setExam(null);
        return this;
    }

    public Set<ProfessorDetails> getSupervisors() {
        return this.supervisors;
    }

    public void setSupervisors(Set<ProfessorDetails> professorDetails) {
        this.supervisors = professorDetails;
    }

    public Exam supervisors(Set<ProfessorDetails> professorDetails) {
        this.setSupervisors(professorDetails);
        return this;
    }

    public Exam addSupervisors(ProfessorDetails professorDetails) {
        this.supervisors.add(professorDetails);
        return this;
    }

    public Exam removeSupervisors(ProfessorDetails professorDetails) {
        this.supervisors.remove(professorDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exam)) {
            return false;
        }
        return getId() != null && getId().equals(((Exam) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exam{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
