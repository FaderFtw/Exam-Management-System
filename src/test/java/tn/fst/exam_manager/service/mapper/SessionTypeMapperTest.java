package tn.fst.exam_manager.service.mapper;

import static tn.fst.exam_manager.domain.SessionTypeAsserts.*;
import static tn.fst.exam_manager.domain.SessionTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SessionTypeMapperTest {

    private SessionTypeMapper sessionTypeMapper;

    @BeforeEach
    void setUp() {
        sessionTypeMapper = new SessionTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSessionTypeSample1();
        var actual = sessionTypeMapper.toEntity(sessionTypeMapper.toDto(expected));
        assertSessionTypeAllPropertiesEquals(expected, actual);
    }
}
