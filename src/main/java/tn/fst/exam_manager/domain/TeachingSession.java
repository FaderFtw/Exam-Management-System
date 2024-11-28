package tn.fst.exam_manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TeachingSession.
 */
@Entity
@Table(name = "teaching_session")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeachingSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "start_hour", nullable = false)
    private Instant startHour;

    @NotNull
    @Column(name = "end_hour", nullable = false)
    private Instant endHour;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teachingSession")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "professors", "teachingSession" }, allowSetters = true)
    private Set<Timetable> timetables = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teachingSession")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "majors", "exam", "teachingSession" }, allowSetters = true)
    private Set<StudentClass> studentClasses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teachingSession")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "departments", "exam", "teachingSession" }, allowSetters = true)
    private Set<Classroom> classrooms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TeachingSession id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TeachingSession name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStartHour() {
        return this.startHour;
    }

    public TeachingSession startHour(Instant startHour) {
        this.setStartHour(startHour);
        return this;
    }

    public void setStartHour(Instant startHour) {
        this.startHour = startHour;
    }

    public Instant getEndHour() {
        return this.endHour;
    }

    public TeachingSession endHour(Instant endHour) {
        this.setEndHour(endHour);
        return this;
    }

    public void setEndHour(Instant endHour) {
        this.endHour = endHour;
    }

    public String getType() {
        return this.type;
    }

    public TeachingSession type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Timetable> getTimetables() {
        return this.timetables;
    }

    public void setTimetables(Set<Timetable> timetables) {
        if (this.timetables != null) {
            this.timetables.forEach(i -> i.setTeachingSession(null));
        }
        if (timetables != null) {
            timetables.forEach(i -> i.setTeachingSession(this));
        }
        this.timetables = timetables;
    }

    public TeachingSession timetables(Set<Timetable> timetables) {
        this.setTimetables(timetables);
        return this;
    }

    public TeachingSession addTimetable(Timetable timetable) {
        this.timetables.add(timetable);
        timetable.setTeachingSession(this);
        return this;
    }

    public TeachingSession removeTimetable(Timetable timetable) {
        this.timetables.remove(timetable);
        timetable.setTeachingSession(null);
        return this;
    }

    public Set<StudentClass> getStudentClasses() {
        return this.studentClasses;
    }

    public void setStudentClasses(Set<StudentClass> studentClasses) {
        if (this.studentClasses != null) {
            this.studentClasses.forEach(i -> i.setTeachingSession(null));
        }
        if (studentClasses != null) {
            studentClasses.forEach(i -> i.setTeachingSession(this));
        }
        this.studentClasses = studentClasses;
    }

    public TeachingSession studentClasses(Set<StudentClass> studentClasses) {
        this.setStudentClasses(studentClasses);
        return this;
    }

    public TeachingSession addStudentClass(StudentClass studentClass) {
        this.studentClasses.add(studentClass);
        studentClass.setTeachingSession(this);
        return this;
    }

    public TeachingSession removeStudentClass(StudentClass studentClass) {
        this.studentClasses.remove(studentClass);
        studentClass.setTeachingSession(null);
        return this;
    }

    public Set<Classroom> getClassrooms() {
        return this.classrooms;
    }

    public void setClassrooms(Set<Classroom> classrooms) {
        if (this.classrooms != null) {
            this.classrooms.forEach(i -> i.setTeachingSession(null));
        }
        if (classrooms != null) {
            classrooms.forEach(i -> i.setTeachingSession(this));
        }
        this.classrooms = classrooms;
    }

    public TeachingSession classrooms(Set<Classroom> classrooms) {
        this.setClassrooms(classrooms);
        return this;
    }

    public TeachingSession addClassroom(Classroom classroom) {
        this.classrooms.add(classroom);
        classroom.setTeachingSession(this);
        return this;
    }

    public TeachingSession removeClassroom(Classroom classroom) {
        this.classrooms.remove(classroom);
        classroom.setTeachingSession(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeachingSession)) {
            return false;
        }
        return getId() != null && getId().equals(((TeachingSession) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeachingSession{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startHour='" + getStartHour() + "'" +
            ", endHour='" + getEndHour() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
