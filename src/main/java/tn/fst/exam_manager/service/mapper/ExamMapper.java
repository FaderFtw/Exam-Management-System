package tn.fst.exam_manager.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import tn.fst.exam_manager.domain.Classroom;
import tn.fst.exam_manager.domain.Exam;
import tn.fst.exam_manager.domain.ExamSession;
import tn.fst.exam_manager.domain.ProfessorDetails;
import tn.fst.exam_manager.domain.StudentClass;
import tn.fst.exam_manager.service.dto.ClassroomDTO;
import tn.fst.exam_manager.service.dto.ExamDTO;
import tn.fst.exam_manager.service.dto.ExamSessionDTO;
import tn.fst.exam_manager.service.dto.ProfessorDetailsDTO;
import tn.fst.exam_manager.service.dto.StudentClassDTO;

/**
 * Mapper for the entity {@link Exam} and its DTO {@link ExamDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExamMapper extends EntityMapper<ExamDTO, Exam> {
    @Mapping(target = "classroom", source = "classroom", qualifiedByName = "classroomId")
    @Mapping(target = "studentClass", source = "studentClass", qualifiedByName = "studentClassId")
    @Mapping(target = "session", source = "session", qualifiedByName = "examSessionId")
    @Mapping(target = "supervisors", source = "supervisors", qualifiedByName = "professorDetailsIdSet")
    ExamDTO toDto(Exam s);

    @Mapping(target = "removeSupervisors", ignore = true)
    Exam toEntity(ExamDTO examDTO);

    @Named("classroomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClassroomDTO toDtoClassroomId(Classroom classroom);

    @Named("studentClassId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudentClassDTO toDtoStudentClassId(StudentClass studentClass);

    @Named("examSessionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamSessionDTO toDtoExamSessionId(ExamSession examSession);

    @Named("professorDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfessorDetailsDTO toDtoProfessorDetailsId(ProfessorDetails professorDetails);

    @Named("professorDetailsIdSet")
    default Set<ProfessorDetailsDTO> toDtoProfessorDetailsIdSet(Set<ProfessorDetails> professorDetails) {
        return professorDetails.stream().map(this::toDtoProfessorDetailsId).collect(Collectors.toSet());
    }
}
