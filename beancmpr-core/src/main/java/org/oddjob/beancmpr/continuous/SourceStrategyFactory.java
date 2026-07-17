package org.oddjob.beancmpr.continuous;

import org.oddjob.beancmpr.Comparer;

/**
 * Something that can create an {@link SourceStrategy}.
 *
 * @param <V> The type of item that will be compared.
 */
public interface SourceStrategyFactory<V> {

    SourceStrategy<V> createStrategy(Comparer<? super V> comparer);
}
