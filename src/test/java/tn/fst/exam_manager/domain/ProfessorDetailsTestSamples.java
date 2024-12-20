package tn.fst.exam_manager.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessorDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProfessorDetails getProfessorDetailsSample1() {
        return new ProfessorDetails().id(1L);
    }

    public static ProfessorDetails getProfessorDetailsSample2() {
        return new ProfessorDetails().id(2L);
    }

    public static ProfessorDetails getProfessorDetailsRandomSampleGenerator() {
        return new ProfessorDetails().id(longCount.incrementAndGet());
    }
}
