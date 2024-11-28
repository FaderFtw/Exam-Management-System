package tn.fst.exam_manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Timetable.
 */
@Entity
@Table(name = "timetable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Timetable implements Serializable {

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
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "timetable")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "supervisedExams", "report", "timetable" }, allowSetters = true)
    private Set<ProfessorDetails> professors = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "timetables", "studentClasses", "classrooms" }, allowSetters = true)
    private TeachingSession teachingSession;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Timetable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Timetable name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Timetable startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Timetable endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<ProfessorDetails> getProfessors() {
        return this.professors;
    }

    public void setProfessors(Set<ProfessorDetails> professorDetails) {
        if (this.professors != null) {
            this.professors.forEach(i -> i.setTimetable(null));
        }
        if (professorDetails != null) {
            professorDetails.forEach(i -> i.setTimetable(this));
        }
        this.professors = professorDetails;
    }

    public Timetable professors(Set<ProfessorDetails> professorDetails) {
        this.setProfessors(professorDetails);
        return this;
    }

    public Timetable addProfessor(ProfessorDetails professorDetails) {
        this.professors.add(professorDetails);
        professorDetails.setTimetable(this);
        return this;
    }

    public Timetable removeProfessor(ProfessorDetails professorDetails) {
        this.professors.remove(professorDetails);
        professorDetails.setTimetable(null);
        return this;
    }

    public TeachingSession getTeachingSession() {
        return this.teachingSession;
    }

    public void setTeachingSession(TeachingSession teachingSession) {
        this.teachingSession = teachingSession;
    }

    public Timetable teachingSession(TeachingSession teachingSession) {
        this.setTeachingSession(teachingSession);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Timetable)) {
            return false;
        }
        return getId() != null && getId().equals(((Timetable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Timetable{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
