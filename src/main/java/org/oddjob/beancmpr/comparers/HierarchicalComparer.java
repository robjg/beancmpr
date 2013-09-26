package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparer;

/**
 * A {@link Comparer} that uses other {@link Comparer}s to create
 * it's comparison. 
 * 
 * @author rob
 *
 */
public interface HierarchicalComparer {

	/**
	 * Used by an owner of comparers to inject the master comparer into
	 * {@link HierarchicalComparer}s.
	 * 
	 * @param parentComparers The master comparers.
	 */
	public void injectComparers(ComparersByType parentComparers);
}
