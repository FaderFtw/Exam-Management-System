package tn.fst.exam_manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClassroomTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Classroom getClassroomSample1() {
        return new Classroom().id(1L).name("name1").capacity(1);
    }

    public static Classroom getClassroomSample2() {
        return new Classroom().id(2L).name("name2").capacity(2);
    }

    public static Classroom getClassroomRandomSampleGenerator() {
        return new Classroom().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).capacity(intCount.incrementAndGet());
    }
}
