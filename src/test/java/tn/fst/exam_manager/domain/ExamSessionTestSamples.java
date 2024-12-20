package tn.fst.exam_manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExamSessionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExamSession getExamSessionSample1() {
        return new ExamSession().id(1L).name("name1").sessionCode("sessionCode1");
    }

    public static ExamSession getExamSessionSample2() {
        return new ExamSession().id(2L).name("name2").sessionCode("sessionCode2");
    }

    public static ExamSession getExamSessionRandomSampleGenerator() {
        return new ExamSession()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .sessionCode(UUID.randomUUID().toString());
    }
}
