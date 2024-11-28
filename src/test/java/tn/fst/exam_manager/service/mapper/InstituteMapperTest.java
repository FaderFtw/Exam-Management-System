package tn.fst.exam_manager.service.mapper;

import static tn.fst.exam_manager.domain.InstituteAsserts.*;
import static tn.fst.exam_manager.domain.InstituteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InstituteMapperTest {

    private InstituteMapper instituteMapper;

    @BeforeEach
    void setUp() {
        instituteMapper = new InstituteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInstituteSample1();
        var actual = instituteMapper.toEntity(instituteMapper.toDto(expected));
        assertInstituteAllPropertiesEquals(expected, actual);
    }
}
