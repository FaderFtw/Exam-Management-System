package tn.fst.exam_manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SessionTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SessionType getSessionTypeSample1() {
        return new SessionType().id(1L).name("name1");
    }

    public static SessionType getSessionTypeSample2() {
        return new SessionType().id(2L).name("name2");
    }

    public static SessionType getSessionTypeRandomSampleGenerator() {
        return new SessionType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
