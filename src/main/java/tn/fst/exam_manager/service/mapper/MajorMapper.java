package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.Major;
import tn.fst.exam_manager.domain.StudentClass;
import tn.fst.exam_manager.service.dto.MajorDTO;
import tn.fst.exam_manager.service.dto.StudentClassDTO;

/**
 * Mapper for the entity {@link Major} and its DTO {@link MajorDTO}.
 */
@Mapper(componentModel = "spring")
public interface MajorMapper extends EntityMapper<MajorDTO, Major> {
    @Mapping(target = "studentClass", source = "studentClass", qualifiedByName = "studentClassId")
    MajorDTO toDto(Major s);

    @Named("studentClassId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudentClassDTO toDtoStudentClassId(StudentClass studentClass);
}
