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
 * A Report.
 */
@Entity
@Table(name = "report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Report implements Serializable {

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
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Lob
    @Column(name = "content")
    private String content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "supervisedExams", "report", "timetable" }, allowSetters = true)
    private Set<ProfessorDetails> professors = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sessionType", "departments", "exam", "report" }, allowSetters = true)
    private Set<ExamSession> examSessions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "institutes", "users", "examSessions", "classroom", "major", "report" }, allowSetters = true)
    private Set<Department> departments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "department", "report" }, allowSetters = true)
    private Set<Institute> institutes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Report id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Report name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Report createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getContent() {
        return this.content;
    }

    public Report content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<ProfessorDetails> getProfessors() {
        return this.professors;
    }

    public void setProfessors(Set<ProfessorDetails> professorDetails) {
        if (this.professors != null) {
            this.professors.forEach(i -> i.setReport(null));
        }
        if (professorDetails != null) {
            professorDetails.forEach(i -> i.setReport(this));
        }
        this.professors = professorDetails;
    }

    public Report professors(Set<ProfessorDetails> professorDetails) {
        this.setProfessors(professorDetails);
        return this;
    }

    public Report addProfessor(ProfessorDetails professorDetails) {
        this.professors.add(professorDetails);
        professorDetails.setReport(this);
        return this;
    }

    public Report removeProfessor(ProfessorDetails professorDetails) {
        this.professors.remove(professorDetails);
        professorDetails.setReport(null);
        return this;
    }

    public Set<ExamSession> getExamSessions() {
        return this.examSessions;
    }

    public void setExamSessions(Set<ExamSession> examSessions) {
        if (this.examSessions != null) {
            this.examSessions.forEach(i -> i.setReport(null));
        }
        if (examSessions != null) {
            examSessions.forEach(i -> i.setReport(this));
        }
        this.examSessions = examSessions;
    }

    public Report examSessions(Set<ExamSession> examSessions) {
        this.setExamSessions(examSessions);
        return this;
    }

    public Report addExamSession(ExamSession examSession) {
        this.examSessions.add(examSession);
        examSession.setReport(this);
        return this;
    }

    public Report removeExamSession(ExamSession examSession) {
        this.examSessions.remove(examSession);
        examSession.setReport(null);
        return this;
    }

    public Set<Department> getDepartments() {
        return this.departments;
    }

    public void setDepartments(Set<Department> departments) {
        if (this.departments != null) {
            this.departments.forEach(i -> i.setReport(null));
        }
        if (departments != null) {
            departments.forEach(i -> i.setReport(this));
        }
        this.departments = departments;
    }

    public Report departments(Set<Department> departments) {
        this.setDepartments(departments);
        return this;
    }

    public Report addDepartment(Department department) {
        this.departments.add(department);
        department.setReport(this);
        return this;
    }

    public Report removeDepartment(Department department) {
        this.departments.remove(department);
        department.setReport(null);
        return this;
    }

    public Set<Institute> getInstitutes() {
        return this.institutes;
    }

    public void setInstitutes(Set<Institute> institutes) {
        if (this.institutes != null) {
            this.institutes.forEach(i -> i.setReport(null));
        }
        if (institutes != null) {
            institutes.forEach(i -> i.setReport(this));
        }
        this.institutes = institutes;
    }

    public Report institutes(Set<Institute> institutes) {
        this.setInstitutes(institutes);
        return this;
    }

    public Report addInstitute(Institute institute) {
        this.institutes.add(institute);
        institute.setReport(this);
        return this;
    }

    public Report removeInstitute(Institute institute) {
        this.institutes.remove(institute);
        institute.setReport(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        return getId() != null && getId().equals(((Report) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
