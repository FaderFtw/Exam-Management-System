package tn.fst.exam_manager.service.mapper;

import static tn.fst.exam_manager.domain.ReportAsserts.*;
import static tn.fst.exam_manager.domain.ReportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportMapperTest {

    private ReportMapper reportMapper;

    @BeforeEach
    void setUp() {
        reportMapper = new ReportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportSample1();
        var actual = reportMapper.toEntity(reportMapper.toDto(expected));
        assertReportAllPropertiesEquals(expected, actual);
    }
}
