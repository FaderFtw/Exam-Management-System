package tn.fst.exam_manager.service.mapper;

import static tn.fst.exam_manager.domain.ExamSessionAsserts.*;
import static tn.fst.exam_manager.domain.ExamSessionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExamSessionMapperTest {

    private ExamSessionMapper examSessionMapper;

    @BeforeEach
    void setUp() {
        examSessionMapper = new ExamSessionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExamSessionSample1();
        var actual = examSessionMapper.toEntity(examSessionMapper.toDto(expected));
        assertExamSessionAllPropertiesEquals(expected, actual);
    }
}
