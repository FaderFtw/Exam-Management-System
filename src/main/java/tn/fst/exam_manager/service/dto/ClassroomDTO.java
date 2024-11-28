package tn.fst.exam_manager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link tn.fst.exam_manager.domain.Classroom} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassroomDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer capacity;

    private ExamDTO exam;

    private TeachingSessionDTO teachingSession;

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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public ExamDTO getExam() {
        return exam;
    }

    public void setExam(ExamDTO exam) {
        this.exam = exam;
    }

    public TeachingSessionDTO getTeachingSession() {
        return teachingSession;
    }

    public void setTeachingSession(TeachingSessionDTO teachingSession) {
        this.teachingSession = teachingSession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassroomDTO)) {
            return false;
        }

        ClassroomDTO classroomDTO = (ClassroomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classroomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassroomDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", capacity=" + getCapacity() +
            ", exam=" + getExam() +
            ", teachingSession=" + getTeachingSession() +
            "}";
    }
}
