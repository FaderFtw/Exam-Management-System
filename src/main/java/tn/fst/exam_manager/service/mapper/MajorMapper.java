package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.Department;
import tn.fst.exam_manager.domain.Major;
import tn.fst.exam_manager.service.dto.DepartmentDTO;
import tn.fst.exam_manager.service.dto.MajorDTO;

/**
 * Mapper for the entity {@link Major} and its DTO {@link MajorDTO}.
 */
@Mapper(componentModel = "spring")
public interface MajorMapper extends EntityMapper<MajorDTO, Major> {
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentId")
    MajorDTO toDto(Major s);

    @Named("departmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoDepartmentId(Department department);
}
