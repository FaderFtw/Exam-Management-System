package tn.fst.exam_manager.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import tn.fst.exam_manager.domain.Classroom;
import tn.fst.exam_manager.domain.Department;
import tn.fst.exam_manager.domain.ExamSession;
import tn.fst.exam_manager.domain.Major;
import tn.fst.exam_manager.domain.Report;
import tn.fst.exam_manager.domain.User;
import tn.fst.exam_manager.service.dto.ClassroomDTO;
import tn.fst.exam_manager.service.dto.DepartmentDTO;
import tn.fst.exam_manager.service.dto.ExamSessionDTO;
import tn.fst.exam_manager.service.dto.MajorDTO;
import tn.fst.exam_manager.service.dto.ReportDTO;
import tn.fst.exam_manager.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {
    @Mapping(target = "users", source = "users", qualifiedByName = "userId")
    @Mapping(target = "examSessions", source = "examSessions", qualifiedByName = "examSessionIdSet")
    @Mapping(target = "classroom", source = "classroom", qualifiedByName = "classroomId")
    @Mapping(target = "major", source = "major", qualifiedByName = "majorId")
    @Mapping(target = "report", source = "report", qualifiedByName = "reportId")
    DepartmentDTO toDto(Department s);

    @Mapping(target = "examSessions", ignore = true)
    @Mapping(target = "removeExamSessions", ignore = true)
    Department toEntity(DepartmentDTO departmentDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("examSessionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamSessionDTO toDtoExamSessionId(ExamSession examSession);

    @Named("examSessionIdSet")
    default Set<ExamSessionDTO> toDtoExamSessionIdSet(Set<ExamSession> examSession) {
        return examSession.stream().map(this::toDtoExamSessionId).collect(Collectors.toSet());
    }

    @Named("classroomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClassroomDTO toDtoClassroomId(Classroom classroom);

    @Named("majorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MajorDTO toDtoMajorId(Major major);

    @Named("reportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportDTO toDtoReportId(Report report);
}
