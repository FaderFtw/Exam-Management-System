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
 * A StudentClass.
 */
@Entity
@Table(name = "student_class")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "student_count")
    private Integer studentCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "studentClass")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "departments", "studentClass" }, allowSetters = true)
    private Set<Major> majors = new HashSet<>();

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

    public StudentClass id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public StudentClass name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStudentCount() {
        return this.studentCount;
    }

    public StudentClass studentCount(Integer studentCount) {
        this.setStudentCount(studentCount);
        return this;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public Set<Major> getMajors() {
        return this.majors;
    }

    public void setMajors(Set<Major> majors) {
        if (this.majors != null) {
            this.majors.forEach(i -> i.setStudentClass(null));
        }
        if (majors != null) {
            majors.forEach(i -> i.setStudentClass(this));
        }
        this.majors = majors;
    }

    public StudentClass majors(Set<Major> majors) {
        this.setMajors(majors);
        return this;
    }

    public StudentClass addMajor(Major major) {
        this.majors.add(major);
        major.setStudentClass(this);
        return this;
    }

    public StudentClass removeMajor(Major major) {
        this.majors.remove(major);
        major.setStudentClass(null);
        return this;
    }

    public Exam getExam() {
        return this.exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public StudentClass exam(Exam exam) {
        this.setExam(exam);
        return this;
    }

    public TeachingSession getTeachingSession() {
        return this.teachingSession;
    }

    public void setTeachingSession(TeachingSession teachingSession) {
        this.teachingSession = teachingSession;
    }

    public StudentClass teachingSession(TeachingSession teachingSession) {
        this.setTeachingSession(teachingSession);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentClass)) {
            return false;
        }
        return getId() != null && getId().equals(((StudentClass) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentClass{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", studentCount=" + getStudentCount() +
            "}";
    }
}
