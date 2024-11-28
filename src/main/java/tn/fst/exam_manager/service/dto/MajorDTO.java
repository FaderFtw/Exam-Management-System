package tn.fst.exam_manager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link tn.fst.exam_manager.domain.Major} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MajorDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private StudentClassDTO studentClass;

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

    public StudentClassDTO getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(StudentClassDTO studentClass) {
        this.studentClass = studentClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MajorDTO)) {
            return false;
        }

        MajorDTO majorDTO = (MajorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, majorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MajorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", studentClass=" + getStudentClass() +
            "}";
    }
}
