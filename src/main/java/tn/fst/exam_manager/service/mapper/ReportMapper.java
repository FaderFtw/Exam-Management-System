package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.Report;
import tn.fst.exam_manager.service.dto.ReportDTO;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {}
