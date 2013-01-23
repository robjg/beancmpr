package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.Comparison;

/**
 * The {@link Comparison}s between the values of two {@link Matchable}s.
 * 
 * @author Rob
 *
 */
public interface MultiValueComparison<T> extends Comparison<T> {

	/**
	 * Provides an {@code Iterable} of the individual {@link Comparison}s
	 * between the values of two {@link Matchable}s.
	 * 
	 * @return
	 */
	public Iterable<Comparison<?>> getValueComparisons();

}
