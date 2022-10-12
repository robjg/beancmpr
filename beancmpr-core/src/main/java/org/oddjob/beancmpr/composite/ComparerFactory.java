package org.oddjob.beancmpr.composite;

import org.oddjob.beancmpr.Comparer;

/**
 * Create an {@link Comparer} which is typically for a complicated type that will delegate to
 * the provided comparers.
 *
 * @param <T>
 */
public interface ComparerFactory<T> {

	/**
	 * Create a comparer with the give item comparers.
	 *
	 * @param comparersByType The item comparers.
	 * @return A comparer for this ype.
	 */
	Comparer<T> createComparerWith(
			ComparersByType comparersByType);
}
