package tn.fst.exam_manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TeachingSessionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TeachingSession getTeachingSessionSample1() {
        return new TeachingSession().id(1L).name("name1").type("type1");
    }

    public static TeachingSession getTeachingSessionSample2() {
        return new TeachingSession().id(2L).name("name2").type("type2");
    }

    public static TeachingSession getTeachingSessionRandomSampleGenerator() {
        return new TeachingSession().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).type(UUID.randomUUID().toString());
    }
}
