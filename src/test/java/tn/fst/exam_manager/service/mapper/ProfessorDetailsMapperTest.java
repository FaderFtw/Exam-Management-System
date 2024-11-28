package tn.fst.exam_manager.service.mapper;

import static tn.fst.exam_manager.domain.ProfessorDetailsAsserts.*;
import static tn.fst.exam_manager.domain.ProfessorDetailsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessorDetailsMapperTest {

    private ProfessorDetailsMapper professorDetailsMapper;

    @BeforeEach
    void setUp() {
        professorDetailsMapper = new ProfessorDetailsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessorDetailsSample1();
        var actual = professorDetailsMapper.toEntity(professorDetailsMapper.toDto(expected));
        assertProfessorDetailsAllPropertiesEquals(expected, actual);
    }
}
