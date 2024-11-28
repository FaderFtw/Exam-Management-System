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
 * A Department.
 */
@Entity
@Table(name = "department")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "department", "report" }, allowSetters = true)
    private Set<Institute> institutes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User users;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "departments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sessionType", "departments", "exam", "report" }, allowSetters = true)
    private Set<ExamSession> examSessions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "departments", "exam", "teachingSession" }, allowSetters = true)
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "departments", "studentClass" }, allowSetters = true)
    private Major major;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "professors", "examSessions", "departments", "institutes" }, allowSetters = true)
    private Report report;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Department id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Department name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Department email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Institute> getInstitutes() {
        return this.institutes;
    }

    public void setInstitutes(Set<Institute> institutes) {
        if (this.institutes != null) {
            this.institutes.forEach(i -> i.setDepartment(null));
        }
        if (institutes != null) {
            institutes.forEach(i -> i.setDepartment(this));
        }
        this.institutes = institutes;
    }

    public Department institutes(Set<Institute> institutes) {
        this.setInstitutes(institutes);
        return this;
    }

    public Department addInstitute(Institute institute) {
        this.institutes.add(institute);
        institute.setDepartment(this);
        return this;
    }

    public Department removeInstitute(Institute institute) {
        this.institutes.remove(institute);
        institute.setDepartment(null);
        return this;
    }

    public User getUsers() {
        return this.users;
    }

    public void setUsers(User user) {
        this.users = user;
    }

    public Department users(User user) {
        this.setUsers(user);
        return this;
    }

    public Set<ExamSession> getExamSessions() {
        return this.examSessions;
    }

    public void setExamSessions(Set<ExamSession> examSessions) {
        if (this.examSessions != null) {
            this.examSessions.forEach(i -> i.removeDepartments(this));
        }
        if (examSessions != null) {
            examSessions.forEach(i -> i.addDepartments(this));
        }
        this.examSessions = examSessions;
    }

    public Department examSessions(Set<ExamSession> examSessions) {
        this.setExamSessions(examSessions);
        return this;
    }

    public Department addExamSessions(ExamSession examSession) {
        this.examSessions.add(examSession);
        examSession.getDepartments().add(this);
        return this;
    }

    public Department removeExamSessions(ExamSession examSession) {
        this.examSessions.remove(examSession);
        examSession.getDepartments().remove(this);
        return this;
    }

    public Classroom getClassroom() {
        return this.classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Department classroom(Classroom classroom) {
        this.setClassroom(classroom);
        return this;
    }

    public Major getMajor() {
        return this.major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public Department major(Major major) {
        this.setMajor(major);
        return this;
    }

    public Report getReport() {
        return this.report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Department report(Report report) {
        this.setReport(report);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }
        return getId() != null && getId().equals(((Department) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Department{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
