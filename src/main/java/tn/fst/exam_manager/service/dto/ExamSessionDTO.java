package tn.fst.exam_manager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link tn.fst.exam_manager.domain.ExamSession} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamSessionDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String sessionCode;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Boolean allowParallelStudies;

    @NotNull
    private Boolean allowOwnClassSupervision;

    @NotNull
    private Boolean allowCombineClasses;

    private SessionTypeDTO sessionType;

    private Set<DepartmentDTO> departments = new HashSet<>();

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

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getAllowParallelStudies() {
        return allowParallelStudies;
    }

    public void setAllowParallelStudies(Boolean allowParallelStudies) {
        this.allowParallelStudies = allowParallelStudies;
    }

    public Boolean getAllowOwnClassSupervision() {
        return allowOwnClassSupervision;
    }

    public void setAllowOwnClassSupervision(Boolean allowOwnClassSupervision) {
        this.allowOwnClassSupervision = allowOwnClassSupervision;
    }

    public Boolean getAllowCombineClasses() {
        return allowCombineClasses;
    }

    public void setAllowCombineClasses(Boolean allowCombineClasses) {
        this.allowCombineClasses = allowCombineClasses;
    }

    public SessionTypeDTO getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionTypeDTO sessionType) {
        this.sessionType = sessionType;
    }

    public Set<DepartmentDTO> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<DepartmentDTO> departments) {
        this.departments = departments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamSessionDTO)) {
            return false;
        }

        ExamSessionDTO examSessionDTO = (ExamSessionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examSessionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamSessionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sessionCode='" + getSessionCode() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", allowParallelStudies='" + getAllowParallelStudies() + "'" +
            ", allowOwnClassSupervision='" + getAllowOwnClassSupervision() + "'" +
            ", allowCombineClasses='" + getAllowCombineClasses() + "'" +
            ", sessionType=" + getSessionType() +
            ", departments=" + getDepartments() +
            "}";
    }
}
