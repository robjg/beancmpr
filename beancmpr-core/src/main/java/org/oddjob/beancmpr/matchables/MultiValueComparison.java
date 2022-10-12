package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.Comparison;

/**
 * A {@link Comparison}s that is the result of a number of comparisons 
 * between two things. The things will often be two {@link Matchable}s.
 * 
 * @see MultiValueComparer
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
	Iterable<Comparison<?>> getValueComparisons();

}
