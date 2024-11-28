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
 * A Classroom.
 */
@Entity
@Table(name = "classroom")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Classroom implements Serializable {

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
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classroom")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "institutes", "users", "examSessions", "classroom", "major", "report" }, allowSetters = true)
    private Set<Department> departments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "classrooms", "studentClasses", "sessions", "supervisors" }, allowSetters = true)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "timetables", "studentClasses", "classrooms" }, allowSetters = true)
    private TeachingSession teachingSession;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Classroom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Classroom name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public Classroom capacity(Integer capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Set<Department> getDepartments() {
        return this.departments;
    }

    public void setDepartments(Set<Department> departments) {
        if (this.departments != null) {
            this.departments.forEach(i -> i.setClassroom(null));
        }
        if (departments != null) {
            departments.forEach(i -> i.setClassroom(this));
        }
        this.departments = departments;
    }

    public Classroom departments(Set<Department> departments) {
        this.setDepartments(departments);
        return this;
    }

    public Classroom addDepartment(Department department) {
        this.departments.add(department);
        department.setClassroom(this);
        return this;
    }

    public Classroom removeDepartment(Department department) {
        this.departments.remove(department);
        department.setClassroom(null);
        return this;
    }

    public Exam getExam() {
        return this.exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Classroom exam(Exam exam) {
        this.setExam(exam);
        return this;
    }

    public TeachingSession getTeachingSession() {
        return this.teachingSession;
    }

    public void setTeachingSession(TeachingSession teachingSession) {
        this.teachingSession = teachingSession;
    }

    public Classroom teachingSession(TeachingSession teachingSession) {
        this.setTeachingSession(teachingSession);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Classroom)) {
            return false;
        }
        return getId() != null && getId().equals(((Classroom) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Classroom{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", capacity=" + getCapacity() +
            "}";
    }
}
