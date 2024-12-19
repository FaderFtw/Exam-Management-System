package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.Department;
import tn.fst.exam_manager.domain.ExamSession;
import tn.fst.exam_manager.domain.Institute;
import tn.fst.exam_manager.domain.ProfessorDetails;
import tn.fst.exam_manager.domain.Report;
import tn.fst.exam_manager.service.dto.DepartmentDTO;
import tn.fst.exam_manager.service.dto.ExamSessionDTO;
import tn.fst.exam_manager.service.dto.InstituteDTO;
import tn.fst.exam_manager.service.dto.ProfessorDetailsDTO;
import tn.fst.exam_manager.service.dto.ReportDTO;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {
    @Mapping(target = "professor", source = "professor", qualifiedByName = "professorDetailsId")
    @Mapping(target = "examSession", source = "examSession", qualifiedByName = "examSessionId")
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentId")
    @Mapping(target = "institute", source = "institute", qualifiedByName = "instituteId")
    ReportDTO toDto(Report s);

    @Named("professorDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfessorDetailsDTO toDtoProfessorDetailsId(ProfessorDetails professorDetails);

    @Named("examSessionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamSessionDTO toDtoExamSessionId(ExamSession examSession);

    @Named("departmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoDepartmentId(Department department);

    @Named("instituteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InstituteDTO toDtoInstituteId(Institute institute);
}
