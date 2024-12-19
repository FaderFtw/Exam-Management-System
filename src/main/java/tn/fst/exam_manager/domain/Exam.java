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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "department" }, allowSetters = true)
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "major" }, allowSetters = true)
    private StudentClass studentClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sessionType", "departments" }, allowSetters = true)
    private ExamSession session;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_exam__supervisors",
        joinColumns = @JoinColumn(name = "exam_id"),
        inverseJoinColumns = @JoinColumn(name = "supervisors_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "supervisedExams" }, allowSetters = true)
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

    public Classroom getClassroom() {
        return this.classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Exam classroom(Classroom classroom) {
        this.setClassroom(classroom);
        return this;
    }

    public StudentClass getStudentClass() {
        return this.studentClass;
    }

    public void setStudentClass(StudentClass studentClass) {
        this.studentClass = studentClass;
    }

    public Exam studentClass(StudentClass studentClass) {
        this.setStudentClass(studentClass);
        return this;
    }

    public ExamSession getSession() {
        return this.session;
    }

    public void setSession(ExamSession examSession) {
        this.session = examSession;
    }

    public Exam session(ExamSession examSession) {
        this.setSession(examSession);
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
