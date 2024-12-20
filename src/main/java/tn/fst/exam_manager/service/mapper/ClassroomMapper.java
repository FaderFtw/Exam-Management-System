package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.Classroom;
import tn.fst.exam_manager.domain.Department;
import tn.fst.exam_manager.service.dto.ClassroomDTO;
import tn.fst.exam_manager.service.dto.DepartmentDTO;

/**
 * Mapper for the entity {@link Classroom} and its DTO {@link ClassroomDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClassroomMapper extends EntityMapper<ClassroomDTO, Classroom> {
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentId")
    ClassroomDTO toDto(Classroom s);

    @Named("departmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoDepartmentId(Department department);
}
