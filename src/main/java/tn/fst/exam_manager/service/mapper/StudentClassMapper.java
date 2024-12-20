package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.Major;
import tn.fst.exam_manager.domain.StudentClass;
import tn.fst.exam_manager.service.dto.MajorDTO;
import tn.fst.exam_manager.service.dto.StudentClassDTO;

/**
 * Mapper for the entity {@link StudentClass} and its DTO {@link StudentClassDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentClassMapper extends EntityMapper<StudentClassDTO, StudentClass> {
    @Mapping(target = "major", source = "major", qualifiedByName = "majorId")
    StudentClassDTO toDto(StudentClass s);

    @Named("majorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MajorDTO toDtoMajorId(Major major);
}
