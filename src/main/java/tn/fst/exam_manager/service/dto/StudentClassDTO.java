package tn.fst.exam_manager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link tn.fst.exam_manager.domain.StudentClass} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentClassDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer studentCount;

    private MajorDTO major;

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

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public MajorDTO getMajor() {
        return major;
    }

    public void setMajor(MajorDTO major) {
        this.major = major;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentClassDTO)) {
            return false;
        }

        StudentClassDTO studentClassDTO = (StudentClassDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentClassDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentClassDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", studentCount=" + getStudentCount() +
            ", major=" + getMajor() +
            "}";
    }
}
