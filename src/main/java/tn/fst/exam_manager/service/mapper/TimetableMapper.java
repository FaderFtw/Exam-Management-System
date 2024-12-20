package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.ProfessorDetails;
import tn.fst.exam_manager.domain.Timetable;
import tn.fst.exam_manager.service.dto.ProfessorDetailsDTO;
import tn.fst.exam_manager.service.dto.TimetableDTO;

/**
 * Mapper for the entity {@link Timetable} and its DTO {@link TimetableDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimetableMapper extends EntityMapper<TimetableDTO, Timetable> {
    @Mapping(target = "professor", source = "professor", qualifiedByName = "professorDetailsId")
    TimetableDTO toDto(Timetable s);

    @Named("professorDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfessorDetailsDTO toDtoProfessorDetailsId(ProfessorDetails professorDetails);
}
