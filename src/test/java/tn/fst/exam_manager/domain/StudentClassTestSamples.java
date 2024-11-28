package tn.fst.exam_manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StudentClassTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StudentClass getStudentClassSample1() {
        return new StudentClass().id(1L).name("name1").studentCount(1);
    }

    public static StudentClass getStudentClassSample2() {
        return new StudentClass().id(2L).name("name2").studentCount(2);
    }

    public static StudentClass getStudentClassRandomSampleGenerator() {
        return new StudentClass()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .studentCount(intCount.incrementAndGet());
    }
}
