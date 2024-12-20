package tn.fst.exam_manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Report getReportSample1() {
        return new Report().id(1L).name("name1");
    }

    public static Report getReportSample2() {
        return new Report().id(2L).name("name2");
    }

    public static Report getReportRandomSampleGenerator() {
        return new Report().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
