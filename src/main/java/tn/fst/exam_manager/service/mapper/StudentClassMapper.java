package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.Exam;
import tn.fst.exam_manager.domain.StudentClass;
import tn.fst.exam_manager.domain.TeachingSession;
import tn.fst.exam_manager.service.dto.ExamDTO;
import tn.fst.exam_manager.service.dto.StudentClassDTO;
import tn.fst.exam_manager.service.dto.TeachingSessionDTO;

/**
 * Mapper for the entity {@link StudentClass} and its DTO {@link StudentClassDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentClassMapper extends EntityMapper<StudentClassDTO, StudentClass> {
    @Mapping(target = "exam", source = "exam", qualifiedByName = "examId")
    @Mapping(target = "teachingSession", source = "teachingSession", qualifiedByName = "teachingSessionId")
    StudentClassDTO toDto(StudentClass s);

    @Named("examId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamDTO toDtoExamId(Exam exam);

    @Named("teachingSessionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TeachingSessionDTO toDtoTeachingSessionId(TeachingSession teachingSession);
}
