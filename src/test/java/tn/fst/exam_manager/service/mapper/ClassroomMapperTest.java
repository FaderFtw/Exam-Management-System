package tn.fst.exam_manager.service.mapper;

import static tn.fst.exam_manager.domain.ClassroomAsserts.*;
import static tn.fst.exam_manager.domain.ClassroomTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassroomMapperTest {

    private ClassroomMapper classroomMapper;

    @BeforeEach
    void setUp() {
        classroomMapper = new ClassroomMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClassroomSample1();
        var actual = classroomMapper.toEntity(classroomMapper.toDto(expected));
        assertClassroomAllPropertiesEquals(expected, actual);
    }
}
