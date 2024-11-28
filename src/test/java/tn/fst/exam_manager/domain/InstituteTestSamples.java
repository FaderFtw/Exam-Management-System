package tn.fst.exam_manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InstituteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Institute getInstituteSample1() {
        return new Institute().id(1L).name("name1").location("location1").phone("phone1").email("email1").website("website1");
    }

    public static Institute getInstituteSample2() {
        return new Institute().id(2L).name("name2").location("location2").phone("phone2").email("email2").website("website2");
    }

    public static Institute getInstituteRandomSampleGenerator() {
        return new Institute()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString());
    }
}
