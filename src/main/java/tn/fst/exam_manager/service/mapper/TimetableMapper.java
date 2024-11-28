package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.TeachingSession;
import tn.fst.exam_manager.domain.Timetable;
import tn.fst.exam_manager.service.dto.TeachingSessionDTO;
import tn.fst.exam_manager.service.dto.TimetableDTO;

/**
 * Mapper for the entity {@link Timetable} and its DTO {@link TimetableDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimetableMapper extends EntityMapper<TimetableDTO, Timetable> {
    @Mapping(target = "teachingSession", source = "teachingSession", qualifiedByName = "teachingSessionId")
    TimetableDTO toDto(Timetable s);

    @Named("teachingSessionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TeachingSessionDTO toDtoTeachingSessionId(TeachingSession teachingSession);
}
