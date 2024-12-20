package tn.fst.exam_manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "professor" }, allowSetters = true)
    private Timetable timetable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "major" }, allowSetters = true)
    private StudentClass studentClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "department" }, allowSetters = true)
    private Classroom classroom;

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

    public Timetable getTimetable() {
        return this.timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public TeachingSession timetable(Timetable timetable) {
        this.setTimetable(timetable);
        return this;
    }

    public StudentClass getStudentClass() {
        return this.studentClass;
    }

    public void setStudentClass(StudentClass studentClass) {
        this.studentClass = studentClass;
    }

    public TeachingSession studentClass(StudentClass studentClass) {
        this.setStudentClass(studentClass);
        return this;
    }

    public Classroom getClassroom() {
        return this.classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public TeachingSession classroom(Classroom classroom) {
        this.setClassroom(classroom);
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
