package tn.fst.exam_manager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import tn.fst.exam_manager.domain.enumeration.Rank;

/**
 * A DTO for the {@link tn.fst.exam_manager.domain.ProfessorDetails} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessorDetailsDTO implements Serializable {

    private Long id;

    @NotNull
    private Rank rank;

    private UserDTO user;

    private Set<ExamDTO> supervisedExams = new HashSet<>();

    private ReportDTO report;

    private TimetableDTO timetable;

    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<ExamDTO> getSupervisedExams() {
        return supervisedExams;
    }

    public void setSupervisedExams(Set<ExamDTO> supervisedExams) {
        this.supervisedExams = supervisedExams;
    }

    public ReportDTO getReport() {
        return report;
    }

    public void setReport(ReportDTO report) {
        this.report = report;
    }

    public TimetableDTO getTimetable() {
        return timetable;
    }

    public void setTimetable(TimetableDTO timetable) {
        this.timetable = timetable;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessorDetailsDTO)) {
            return false;
        }

        ProfessorDetailsDTO professorDetailsDTO = (ProfessorDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professorDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessorDetailsDTO{" +
            "id=" + getId() +
            ", rank='" + getRank() + "'" +
            ", user=" + getUser() +
            ", supervisedExams=" + getSupervisedExams() +
            ", report=" + getReport() +
            ", timetable=" + getTimetable() +
            "}";
    }
}
