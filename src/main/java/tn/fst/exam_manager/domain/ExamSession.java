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
 * A ExamSession.
 */
@Entity
@Table(name = "exam_session")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamSession implements Serializable {

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
    @Column(name = "session_code", nullable = false)
    private String sessionCode;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "allow_parallel_studies", nullable = false)
    private Boolean allowParallelStudies;

    @NotNull
    @Column(name = "allow_own_class_supervision", nullable = false)
    private Boolean allowOwnClassSupervision;

    @NotNull
    @Column(name = "allow_combine_classes", nullable = false)
    private Boolean allowCombineClasses;

    @JsonIgnoreProperties(value = { "examSession" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private SessionType sessionType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_exam_session__departments",
        joinColumns = @JoinColumn(name = "exam_session_id"),
        inverseJoinColumns = @JoinColumn(name = "departments_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "institutes", "users", "examSessions", "classroom", "major", "report" }, allowSetters = true)
    private Set<Department> departments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "classrooms", "studentClasses", "sessions", "supervisors" }, allowSetters = true)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "professors", "examSessions", "departments", "institutes" }, allowSetters = true)
    private Report report;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExamSession id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ExamSession name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessionCode() {
        return this.sessionCode;
    }

    public ExamSession sessionCode(String sessionCode) {
        this.setSessionCode(sessionCode);
        return this;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public ExamSession startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public ExamSession endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getAllowParallelStudies() {
        return this.allowParallelStudies;
    }

    public ExamSession allowParallelStudies(Boolean allowParallelStudies) {
        this.setAllowParallelStudies(allowParallelStudies);
        return this;
    }

    public void setAllowParallelStudies(Boolean allowParallelStudies) {
        this.allowParallelStudies = allowParallelStudies;
    }

    public Boolean getAllowOwnClassSupervision() {
        return this.allowOwnClassSupervision;
    }

    public ExamSession allowOwnClassSupervision(Boolean allowOwnClassSupervision) {
        this.setAllowOwnClassSupervision(allowOwnClassSupervision);
        return this;
    }

    public void setAllowOwnClassSupervision(Boolean allowOwnClassSupervision) {
        this.allowOwnClassSupervision = allowOwnClassSupervision;
    }

    public Boolean getAllowCombineClasses() {
        return this.allowCombineClasses;
    }

    public ExamSession allowCombineClasses(Boolean allowCombineClasses) {
        this.setAllowCombineClasses(allowCombineClasses);
        return this;
    }

    public void setAllowCombineClasses(Boolean allowCombineClasses) {
        this.allowCombineClasses = allowCombineClasses;
    }

    public SessionType getSessionType() {
        return this.sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public ExamSession sessionType(SessionType sessionType) {
        this.setSessionType(sessionType);
        return this;
    }

    public Set<Department> getDepartments() {
        return this.departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public ExamSession departments(Set<Department> departments) {
        this.setDepartments(departments);
        return this;
    }

    public ExamSession addDepartments(Department department) {
        this.departments.add(department);
        return this;
    }

    public ExamSession removeDepartments(Department department) {
        this.departments.remove(department);
        return this;
    }

    public Exam getExam() {
        return this.exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public ExamSession exam(Exam exam) {
        this.setExam(exam);
        return this;
    }

    public Report getReport() {
        return this.report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public ExamSession report(Report report) {
        this.setReport(report);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamSession)) {
            return false;
        }
        return getId() != null && getId().equals(((ExamSession) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamSession{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sessionCode='" + getSessionCode() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", allowParallelStudies='" + getAllowParallelStudies() + "'" +
            ", allowOwnClassSupervision='" + getAllowOwnClassSupervision() + "'" +
            ", allowCombineClasses='" + getAllowCombineClasses() + "'" +
            "}";
    }
}
