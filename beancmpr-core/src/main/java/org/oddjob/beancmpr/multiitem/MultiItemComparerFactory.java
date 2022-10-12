package org.oddjob.beancmpr.multiitem;

import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.matchables.CompareResultsHandler;

/**
 * Creates an {@link MultiItemComparer}. This is a major Comparer capable of comparing a
 * lot of data such that the results must be streamed out as they are produced. To this
 * end the detail comparison is fed to the provided {@link CompareResultsHandler}.
 *
 * @param <T> The type of thing being compared.
 */
public interface MultiItemComparerFactory<T> {

	/**
	 * Create the Comparer.
	 *
	 * @param comparersByType Item Compares.
	 * @param resultHandler To Stream Detail Results to.
	 * @return The created Comparer.
	 */
	MultiItemComparer<T> createComparerWith(ComparersByType comparersByType,
											CompareResultsHandler resultHandler);
	
}
