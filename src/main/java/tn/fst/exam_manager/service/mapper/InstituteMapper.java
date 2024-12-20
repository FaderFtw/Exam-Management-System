package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.Institute;
import tn.fst.exam_manager.service.dto.InstituteDTO;

/**
 * Mapper for the entity {@link Institute} and its DTO {@link InstituteDTO}.
 */
@Mapper(componentModel = "spring")
public interface InstituteMapper extends EntityMapper<InstituteDTO, Institute> {}
