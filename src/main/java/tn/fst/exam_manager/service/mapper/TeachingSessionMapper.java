package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.TeachingSession;
import tn.fst.exam_manager.service.dto.TeachingSessionDTO;

/**
 * Mapper for the entity {@link TeachingSession} and its DTO {@link TeachingSessionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeachingSessionMapper extends EntityMapper<TeachingSessionDTO, TeachingSession> {}
