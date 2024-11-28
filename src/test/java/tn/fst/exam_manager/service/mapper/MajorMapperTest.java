package tn.fst.exam_manager.service.mapper;

import static tn.fst.exam_manager.domain.MajorAsserts.*;
import static tn.fst.exam_manager.domain.MajorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MajorMapperTest {

    private MajorMapper majorMapper;

    @BeforeEach
    void setUp() {
        majorMapper = new MajorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMajorSample1();
        var actual = majorMapper.toEntity(majorMapper.toDto(expected));
        assertMajorAllPropertiesEquals(expected, actual);
    }
}
