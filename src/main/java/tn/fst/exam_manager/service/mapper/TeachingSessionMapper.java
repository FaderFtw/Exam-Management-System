package tn.fst.exam_manager.service.mapper;

import org.mapstruct.*;
import tn.fst.exam_manager.domain.Classroom;
import tn.fst.exam_manager.domain.StudentClass;
import tn.fst.exam_manager.domain.TeachingSession;
import tn.fst.exam_manager.domain.Timetable;
import tn.fst.exam_manager.service.dto.ClassroomDTO;
import tn.fst.exam_manager.service.dto.StudentClassDTO;
import tn.fst.exam_manager.service.dto.TeachingSessionDTO;
import tn.fst.exam_manager.service.dto.TimetableDTO;

/**
 * Mapper for the entity {@link TeachingSession} and its DTO {@link TeachingSessionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeachingSessionMapper extends EntityMapper<TeachingSessionDTO, TeachingSession> {
    @Mapping(target = "timetable", source = "timetable", qualifiedByName = "timetableId")
    @Mapping(target = "studentClass", source = "studentClass", qualifiedByName = "studentClassId")
    @Mapping(target = "classroom", source = "classroom", qualifiedByName = "classroomId")
    TeachingSessionDTO toDto(TeachingSession s);

    @Named("timetableId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TimetableDTO toDtoTimetableId(Timetable timetable);

    @Named("studentClassId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudentClassDTO toDtoStudentClassId(StudentClass studentClass);

    @Named("classroomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClassroomDTO toDtoClassroomId(Classroom classroom);
}
