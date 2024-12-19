package tn.fst.exam_manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tn.fst.exam_manager.domain.enumeration.Rank;

/**
 * A ProfessorDetails.
 */
@Entity
@Table(name = "professor_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessorDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rank", nullable = false)
    private Rank rank;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "supervisors")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classroom", "studentClass", "session", "supervisors" }, allowSetters = true)
    private Set<Exam> supervisedExams = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfessorDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rank getRank() {
        return this.rank;
    }

    public ProfessorDetails rank(Rank rank) {
        this.setRank(rank);
        return this;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProfessorDetails user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Exam> getSupervisedExams() {
        return this.supervisedExams;
    }

    public void setSupervisedExams(Set<Exam> exams) {
        if (this.supervisedExams != null) {
            this.supervisedExams.forEach(i -> i.removeSupervisors(this));
        }
        if (exams != null) {
            exams.forEach(i -> i.addSupervisors(this));
        }
        this.supervisedExams = exams;
    }

    public ProfessorDetails supervisedExams(Set<Exam> exams) {
        this.setSupervisedExams(exams);
        return this;
    }

    public ProfessorDetails addSupervisedExams(Exam exam) {
        this.supervisedExams.add(exam);
        exam.getSupervisors().add(this);
        return this;
    }

    public ProfessorDetails removeSupervisedExams(Exam exam) {
        this.supervisedExams.remove(exam);
        exam.getSupervisors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessorDetails)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfessorDetails) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessorDetails{" +
            "id=" + getId() +
            ", rank='" + getRank() + "'" +
            "}";
    }
}
