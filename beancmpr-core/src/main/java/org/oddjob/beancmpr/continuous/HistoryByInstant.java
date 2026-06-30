package org.oddjob.beancmpr.continuous;

import java.time.Instant;
import java.time.InstantSource;

/**
 * A {@link SourceHistory} by time.
 *
 * @param <V> The history item type.
 */
public class HistoryByInstant<V> extends HistoryByComparable<Instant, V> {

    public HistoryByInstant(InstantSource clock) {
        super(clock::instant);
    }

}
