package tn.fst.exam_manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MajorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Major getMajorSample1() {
        return new Major().id(1L).name("name1");
    }

    public static Major getMajorSample2() {
        return new Major().id(2L).name("name2");
    }

    public static Major getMajorRandomSampleGenerator() {
        return new Major().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
