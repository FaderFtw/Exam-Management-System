package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.Department;
import tn.fst.exam_manager.domain.Institute;
import tn.fst.exam_manager.domain.User;
import tn.fst.exam_manager.domain.UserAcademicInfo;
import tn.fst.exam_manager.service.dto.DepartmentDTO;
import tn.fst.exam_manager.service.dto.InstituteDTO;
import tn.fst.exam_manager.service.dto.UserAcademicInfoDTO;
import tn.fst.exam_manager.service.dto.UserDTO;

/**
 * Mapper for the entity {@link UserAcademicInfo} and its DTO {@link UserAcademicInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAcademicInfoMapper extends EntityMapper<UserAcademicInfoDTO, UserAcademicInfo> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentName")
    @Mapping(target = "institute", source = "institute", qualifiedByName = "instituteName")
    UserAcademicInfoDTO toDto(UserAcademicInfo s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("departmentName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DepartmentDTO toDtoDepartmentName(Department department);

    @Named("instituteName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    InstituteDTO toDtoInstituteName(Institute institute);
}
