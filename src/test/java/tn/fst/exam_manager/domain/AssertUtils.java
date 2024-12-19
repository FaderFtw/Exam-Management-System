package tn.fst.exam_manager.domain;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;

public class AssertUtils {

    public static Comparator<ZonedDateTime> zonedDataTimeSameInstant = Comparator.nullsFirst(
        Comparator.comparing(e1 -> e1.withZoneSameInstant(ZoneOffset.UTC))
    );

    public static Comparator<BigDecimal> bigDecimalCompareTo = Comparator.nullsFirst(BigDecimal::compareTo);
}
