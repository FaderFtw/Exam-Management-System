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

    private DepartmentDTO department;

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

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
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
            ", department=" + getDepartment() +
            "}";
    }
}
