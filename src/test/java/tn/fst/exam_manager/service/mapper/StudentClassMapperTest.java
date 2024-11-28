package tn.fst.exam_manager.service.mapper;

import static tn.fst.exam_manager.domain.StudentClassAsserts.*;
import static tn.fst.exam_manager.domain.StudentClassTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentClassMapperTest {

    private StudentClassMapper studentClassMapper;

    @BeforeEach
    void setUp() {
        studentClassMapper = new StudentClassMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStudentClassSample1();
        var actual = studentClassMapper.toEntity(studentClassMapper.toDto(expected));
        assertStudentClassAllPropertiesEquals(expected, actual);
    }
}
