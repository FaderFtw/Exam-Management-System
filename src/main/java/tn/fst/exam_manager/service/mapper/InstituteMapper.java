package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.Department;
import tn.fst.exam_manager.domain.Institute;
import tn.fst.exam_manager.domain.Report;
import tn.fst.exam_manager.service.dto.DepartmentDTO;
import tn.fst.exam_manager.service.dto.InstituteDTO;
import tn.fst.exam_manager.service.dto.ReportDTO;

/**
 * Mapper for the entity {@link Institute} and its DTO {@link InstituteDTO}.
 */
@Mapper(componentModel = "spring")
public interface InstituteMapper extends EntityMapper<InstituteDTO, Institute> {
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentId")
    @Mapping(target = "report", source = "report", qualifiedByName = "reportId")
    InstituteDTO toDto(Institute s);

    @Named("departmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoDepartmentId(Department department);

    @Named("reportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportDTO toDtoReportId(Report report);
}
