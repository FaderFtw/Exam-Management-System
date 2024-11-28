package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.Classroom;
import tn.fst.exam_manager.domain.Exam;
import tn.fst.exam_manager.domain.TeachingSession;
import tn.fst.exam_manager.service.dto.ClassroomDTO;
import tn.fst.exam_manager.service.dto.ExamDTO;
import tn.fst.exam_manager.service.dto.TeachingSessionDTO;

/**
 * Mapper for the entity {@link Classroom} and its DTO {@link ClassroomDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClassroomMapper extends EntityMapper<ClassroomDTO, Classroom> {
    @Mapping(target = "exam", source = "exam", qualifiedByName = "examId")
    @Mapping(target = "teachingSession", source = "teachingSession", qualifiedByName = "teachingSessionId")
    ClassroomDTO toDto(Classroom s);

    @Named("examId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamDTO toDtoExamId(Exam exam);

    @Named("teachingSessionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TeachingSessionDTO toDtoTeachingSessionId(TeachingSession teachingSession);
}
