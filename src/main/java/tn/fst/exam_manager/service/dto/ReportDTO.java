package tn.fst.exam_manager.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link tn.fst.exam_manager.domain.Report} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Instant createdDate;

    @Lob
    private String content;

    private ProfessorDetailsDTO professor;

    private ExamSessionDTO examSession;

    private DepartmentDTO department;

    private InstituteDTO institute;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ProfessorDetailsDTO getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorDetailsDTO professor) {
        this.professor = professor;
    }

    public ExamSessionDTO getExamSession() {
        return examSession;
    }

    public void setExamSession(ExamSessionDTO examSession) {
        this.examSession = examSession;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public InstituteDTO getInstitute() {
        return institute;
    }

    public void setInstitute(InstituteDTO institute) {
        this.institute = institute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportDTO)) {
            return false;
        }

        ReportDTO reportDTO = (ReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", content='" + getContent() + "'" +
            ", professor=" + getProfessor() +
            ", examSession=" + getExamSession() +
            ", department=" + getDepartment() +
            ", institute=" + getInstitute() +
            "}";
    }
}
