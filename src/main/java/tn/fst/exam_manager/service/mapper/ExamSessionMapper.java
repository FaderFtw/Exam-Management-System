package tn.fst.exam_manager.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import tn.fst.exam_manager.domain.Department;
import tn.fst.exam_manager.domain.Exam;
import tn.fst.exam_manager.domain.ExamSession;
import tn.fst.exam_manager.domain.Report;
import tn.fst.exam_manager.domain.SessionType;
import tn.fst.exam_manager.service.dto.DepartmentDTO;
import tn.fst.exam_manager.service.dto.ExamDTO;
import tn.fst.exam_manager.service.dto.ExamSessionDTO;
import tn.fst.exam_manager.service.dto.ReportDTO;
import tn.fst.exam_manager.service.dto.SessionTypeDTO;

/**
 * Mapper for the entity {@link ExamSession} and its DTO {@link ExamSessionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExamSessionMapper extends EntityMapper<ExamSessionDTO, ExamSession> {
    @Mapping(target = "sessionType", source = "sessionType", qualifiedByName = "sessionTypeId")
    @Mapping(target = "departments", source = "departments", qualifiedByName = "departmentIdSet")
    @Mapping(target = "exam", source = "exam", qualifiedByName = "examId")
    @Mapping(target = "report", source = "report", qualifiedByName = "reportId")
    ExamSessionDTO toDto(ExamSession s);

    @Mapping(target = "removeDepartments", ignore = true)
    ExamSession toEntity(ExamSessionDTO examSessionDTO);

    @Named("sessionTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SessionTypeDTO toDtoSessionTypeId(SessionType sessionType);

    @Named("departmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoDepartmentId(Department department);

    @Named("departmentIdSet")
    default Set<DepartmentDTO> toDtoDepartmentIdSet(Set<Department> department) {
        return department.stream().map(this::toDtoDepartmentId).collect(Collectors.toSet());
    }

    @Named("examId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamDTO toDtoExamId(Exam exam);

    @Named("reportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportDTO toDtoReportId(Report report);
}
