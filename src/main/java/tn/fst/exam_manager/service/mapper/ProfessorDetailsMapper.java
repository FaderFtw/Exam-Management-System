package tn.fst.exam_manager.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import tn.fst.exam_manager.domain.Exam;
import tn.fst.exam_manager.domain.ProfessorDetails;
import tn.fst.exam_manager.domain.Report;
import tn.fst.exam_manager.domain.Timetable;
import tn.fst.exam_manager.domain.User;
import tn.fst.exam_manager.service.dto.ExamDTO;
import tn.fst.exam_manager.service.dto.ProfessorDetailsDTO;
import tn.fst.exam_manager.service.dto.ReportDTO;
import tn.fst.exam_manager.service.dto.TimetableDTO;
import tn.fst.exam_manager.service.dto.UserDTO;

/**
 * Mapper for the entity {@link ProfessorDetails} and its DTO {@link ProfessorDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessorDetailsMapper extends EntityMapper<ProfessorDetailsDTO, ProfessorDetails> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "supervisedExams", source = "supervisedExams", qualifiedByName = "examIdSet")
    @Mapping(target = "report", source = "report", qualifiedByName = "reportId")
    @Mapping(target = "timetable", source = "timetable", qualifiedByName = "timetableId")
    ProfessorDetailsDTO toDto(ProfessorDetails s);

    @Mapping(target = "supervisedExams", ignore = true)
    @Mapping(target = "removeSupervisedExams", ignore = true)
    ProfessorDetails toEntity(ProfessorDetailsDTO professorDetailsDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("examId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamDTO toDtoExamId(Exam exam);

    @Named("examIdSet")
    default Set<ExamDTO> toDtoExamIdSet(Set<Exam> exam) {
        return exam.stream().map(this::toDtoExamId).collect(Collectors.toSet());
    }

    @Named("reportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportDTO toDtoReportId(Report report);

    @Named("timetableId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TimetableDTO toDtoTimetableId(Timetable timetable);
}
