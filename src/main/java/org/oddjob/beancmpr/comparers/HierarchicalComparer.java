package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparer;

/**
 * A {@link Comparer} that uses other {@link Comparer}s to create
 * it's comparison. 
 * 
 * @author rob
 *
 * @param <T>
 */
public interface HierarchicalComparer {

	/**
	 * Used by owner of comparers to inject the master comparer into
	 * {@link HierarchicalComparer}s.
	 * 
	 * @param comparers The master comparers.
	 */
	public void injectComparers(ComparersByType comparers);
}
