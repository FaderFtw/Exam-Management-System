package tn.fst.exam_manager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link tn.fst.exam_manager.domain.Department} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepartmentDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String email;

    private UserDTO users;

    private Set<ExamSessionDTO> examSessions = new HashSet<>();

    private ClassroomDTO classroom;

    private MajorDTO major;

    private ReportDTO report;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDTO getUsers() {
        return users;
    }

    public void setUsers(UserDTO users) {
        this.users = users;
    }

    public Set<ExamSessionDTO> getExamSessions() {
        return examSessions;
    }

    public void setExamSessions(Set<ExamSessionDTO> examSessions) {
        this.examSessions = examSessions;
    }

    public ClassroomDTO getClassroom() {
        return classroom;
    }

    public void setClassroom(ClassroomDTO classroom) {
        this.classroom = classroom;
    }

    public MajorDTO getMajor() {
        return major;
    }

    public void setMajor(MajorDTO major) {
        this.major = major;
    }

    public ReportDTO getReport() {
        return report;
    }

    public void setReport(ReportDTO report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentDTO)) {
            return false;
        }

        DepartmentDTO departmentDTO = (DepartmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, departmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", users=" + getUsers() +
            ", examSessions=" + getExamSessions() +
            ", classroom=" + getClassroom() +
            ", major=" + getMajor() +
            ", report=" + getReport() +
            "}";
    }
}
