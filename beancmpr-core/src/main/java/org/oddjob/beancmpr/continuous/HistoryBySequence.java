package org.oddjob.beancmpr.continuous;

import java.util.function.Supplier;

/**
 * Provide a history in sequence. Used when time isn't important, just the
 * order of arrival of the items.
 *
 * @param <V> The type of item.
 */
public class HistoryBySequence<V> extends HistoryByComparable<Integer, V> {

    static class Sequence implements Supplier<Integer> {

        private int sequence;

        Sequence(int start) {
            this.sequence = start;
        }

        @Override
        public Integer get() {
            return sequence++;
        }
    }

    public HistoryBySequence() {
        super(new Sequence(1));
    }

    public static <V> HistoryBySequence<V> of(V... values) {

        HistoryBySequence<V> sequence = new HistoryBySequence<>();
        for (V value : values) {
            sequence.store(value);
        }

        return sequence;
    }

    @Override
    public boolean isWithinTolerance(Entry<V> entry) {
        return false;
    }
}
