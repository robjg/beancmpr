package org.oddjob.beancmpr.continuous;

import org.oddjob.beancmpr.Comparison;

import java.util.Optional;

/**
 * Provide a Strategy for comparing items from two different sources
 * arriving at different times. This provides a strategy for one
 * side of the comparison.
 *
 * @param <V> The type of the items.
 */
public interface SourceStrategy<V> {

    Optional<Runnable> onX(V x,
                           SourceHistory<V> xHistory,
                           SourceHistory<V> yHistory,
                           Results<V> results);

    Optional<Runnable> onY(V y,
                           SourceHistory<V> yHistory,
                           SourceHistory<V> xHistory,
                           Results<V> results);

    interface Results<V> {

        void comparison(Comparison<? super V> comparison);

        void theirMissing(V theirItem);
    }
}
