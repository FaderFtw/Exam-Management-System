package tn.fst.exam_manager.service.mapper;

import static tn.fst.exam_manager.domain.TeachingSessionAsserts.*;
import static tn.fst.exam_manager.domain.TeachingSessionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeachingSessionMapperTest {

    private TeachingSessionMapper teachingSessionMapper;

    @BeforeEach
    void setUp() {
        teachingSessionMapper = new TeachingSessionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTeachingSessionSample1();
        var actual = teachingSessionMapper.toEntity(teachingSessionMapper.toDto(expected));
        assertTeachingSessionAllPropertiesEquals(expected, actual);
    }
}
