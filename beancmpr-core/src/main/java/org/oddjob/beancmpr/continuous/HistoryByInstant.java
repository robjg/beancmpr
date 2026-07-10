package org.oddjob.beancmpr.continuous;

import java.time.Duration;
import java.time.Instant;
import java.time.InstantSource;
import java.util.function.Function;

/**
 * A {@link SourceHistory} by time.
 *
 * @param <V> The history item type.
 */
public class HistoryByInstant<V> extends HistoryByComparable<Instant, V> {

    private final Function<Instant, Boolean> withinTolerance;

    public HistoryByInstant(InstantSource clock) {
        this(clock, Duration.ZERO);
    }

    public HistoryByInstant(InstantSource clock,
                            Duration tolerance) {
        super(clock::instant);
        this.withinTolerance = instant -> clock.instant().isBefore(instant.plus(tolerance));
    }

    @Override
    public boolean isWithinTolerance(Entry<V> entry) {
        return withinTolerance.apply(((KeyValue<Instant, V>) entry).getKey());
    }
}
