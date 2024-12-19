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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Institute institute;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "departments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sessionType", "departments" }, allowSetters = true)
    private Set<ExamSession> examSessions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "department", "institute" }, allowSetters = true)
    private Set<UserAcademicInfo> users = new HashSet<>();

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

    public Institute getInstitute() {
        return this.institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public Department institute(Institute institute) {
        this.setInstitute(institute);
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

    public Set<UserAcademicInfo> getUsers() {
        return this.users;
    }

    public void setUsers(Set<UserAcademicInfo> userAcademicInfos) {
        if (this.users != null) {
            this.users.forEach(i -> i.setDepartment(null));
        }
        if (userAcademicInfos != null) {
            userAcademicInfos.forEach(i -> i.setDepartment(this));
        }
        this.users = userAcademicInfos;
    }

    public Department users(Set<UserAcademicInfo> userAcademicInfos) {
        this.setUsers(userAcademicInfos);
        return this;
    }

    public Department addUsers(UserAcademicInfo userAcademicInfo) {
        this.users.add(userAcademicInfo);
        userAcademicInfo.setDepartment(this);
        return this;
    }

    public Department removeUsers(UserAcademicInfo userAcademicInfo) {
        this.users.remove(userAcademicInfo);
        userAcademicInfo.setDepartment(null);
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
