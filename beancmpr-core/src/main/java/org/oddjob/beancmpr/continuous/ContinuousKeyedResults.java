package org.oddjob.beancmpr.continuous;

import org.oddjob.beancmpr.Comparison;

/**
 * Result Callbacks for an {@link ContinuousKeyedComparer}.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface ContinuousKeyedResults<K extends Comparable<K>, V> {

    void xMissing(K key, V value);

    void yMissing(K key, V value);

    void comparison(K key, Comparison<? super V> comparison);

}
