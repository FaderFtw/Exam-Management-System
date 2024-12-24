package tn.fst.exam_manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserAcademicInfoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserAcademicInfo getUserAcademicInfoSample1() {
        return new UserAcademicInfo().id(1L).phone("phone1");
    }

    public static UserAcademicInfo getUserAcademicInfoSample2() {
        return new UserAcademicInfo().id(2L).phone("phone2");
    }

    public static UserAcademicInfo getUserAcademicInfoRandomSampleGenerator() {
        return new UserAcademicInfo().id(longCount.incrementAndGet()).phone(UUID.randomUUID().toString());
    }
}
