package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.SessionType;
import tn.fst.exam_manager.service.dto.SessionTypeDTO;

/**
 * Mapper for the entity {@link SessionType} and its DTO {@link SessionTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface SessionTypeMapper extends EntityMapper<SessionTypeDTO, SessionType> {}
