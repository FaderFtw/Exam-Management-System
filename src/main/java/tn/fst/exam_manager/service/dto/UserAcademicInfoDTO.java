package tn.fst.exam_manager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link tn.fst.exam_manager.domain.UserAcademicInfo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAcademicInfoDTO implements Serializable {

    private Long id;

    @Pattern(regexp = "^\\d{8}$")
    private String phone;

    @NotNull
    private UserDTO user;

    private DepartmentDTO department;

    private InstituteDTO institute;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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
        if (!(o instanceof UserAcademicInfoDTO)) {
            return false;
        }

        UserAcademicInfoDTO userAcademicInfoDTO = (UserAcademicInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAcademicInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAcademicInfoDTO{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            ", user=" + getUser() +
            ", department=" + getDepartment() +
            ", institute=" + getInstitute() +
            "}";
    }
}
