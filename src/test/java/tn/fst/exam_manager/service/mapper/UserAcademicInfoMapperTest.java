package tn.fst.exam_manager.service.mapper;

import static tn.fst.exam_manager.domain.UserAcademicInfoAsserts.*;
import static tn.fst.exam_manager.domain.UserAcademicInfoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAcademicInfoMapperTest {

    private UserAcademicInfoMapper userAcademicInfoMapper;

    @BeforeEach
    void setUp() {
        userAcademicInfoMapper = new UserAcademicInfoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserAcademicInfoSample1();
        var actual = userAcademicInfoMapper.toEntity(userAcademicInfoMapper.toDto(expected));
        assertUserAcademicInfoAllPropertiesEquals(expected, actual);
    }
}
