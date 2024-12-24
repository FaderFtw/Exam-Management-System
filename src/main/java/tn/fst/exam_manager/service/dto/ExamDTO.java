package tn.fst.exam_manager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link tn.fst.exam_manager.domain.Exam} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer numberOfStudents;

    @NotNull
    private Instant startTime;

    @NotNull
    private Instant endTime;

    private ClassroomDTO classroom;

    private StudentClassDTO studentClass;

    private ExamSessionDTO session;

    private Set<ProfessorDetailsDTO> supervisors = new HashSet<>();

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

    public Integer getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public ClassroomDTO getClassroom() {
        return classroom;
    }

    public void setClassroom(ClassroomDTO classroom) {
        this.classroom = classroom;
    }

    public StudentClassDTO getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(StudentClassDTO studentClass) {
        this.studentClass = studentClass;
    }

    public ExamSessionDTO getSession() {
        return session;
    }

    public void setSession(ExamSessionDTO session) {
        this.session = session;
    }

    public Set<ProfessorDetailsDTO> getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(Set<ProfessorDetailsDTO> supervisors) {
        this.supervisors = supervisors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamDTO)) {
            return false;
        }

        ExamDTO examDTO = (ExamDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", numberOfStudents=" + getNumberOfStudents() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", classroom=" + getClassroom() +
            ", studentClass=" + getStudentClass() +
            ", session=" + getSession() +
            ", supervisors=" + getSupervisors() +
            "}";
    }
}
