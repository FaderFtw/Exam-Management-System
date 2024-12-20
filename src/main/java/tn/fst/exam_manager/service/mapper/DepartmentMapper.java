package tn.fst.exam_manager.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import tn.fst.exam_manager.domain.Department;
import tn.fst.exam_manager.domain.ExamSession;
import tn.fst.exam_manager.domain.Institute;
import tn.fst.exam_manager.service.dto.DepartmentDTO;
import tn.fst.exam_manager.service.dto.ExamSessionDTO;
import tn.fst.exam_manager.service.dto.InstituteDTO;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {
    @Mapping(target = "institute", source = "institute", qualifiedByName = "instituteId")
    @Mapping(target = "examSessions", source = "examSessions", qualifiedByName = "examSessionIdSet")
    DepartmentDTO toDto(Department s);

    @Mapping(target = "examSessions", ignore = true)
    @Mapping(target = "removeExamSessions", ignore = true)
    Department toEntity(DepartmentDTO departmentDTO);

    @Named("instituteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InstituteDTO toDtoInstituteId(Institute institute);

    @Named("examSessionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamSessionDTO toDtoExamSessionId(ExamSession examSession);

    @Named("examSessionIdSet")
    default Set<ExamSessionDTO> toDtoExamSessionIdSet(Set<ExamSession> examSession) {
        return examSession.stream().map(this::toDtoExamSessionId).collect(Collectors.toSet());
    }
}
