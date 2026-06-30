package org.oddjob.beancmpr.continuous;

import org.oddjob.beancmpr.Comparison;

/**
 * Result call backs for a {@link ContinuousComparer}
 *
 * @param <V> The type of item being compared.
 */
public interface ContinuousResults<V> {

    void xMissing(V value);

    void yMissing(V value);

    void comparison(Comparison<? super V> comparison);

}
