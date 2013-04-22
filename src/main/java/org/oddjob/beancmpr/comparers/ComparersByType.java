package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparer;

/**
 * A collection of {@link Comparer}s.
 * 
 * @author rob
 *
 */
public interface ComparersByType extends HierarchicalComparer {

	/**
	 * Find a {@link Comparer} for a given type.
	 * 
	 * @param <T> The type of the type.
	 * @param type The type.
	 * 
	 * @return The comparer or null if one can't be found for the type.
	 */
	public <T> Comparer<T> comparerFor(Class<T> type);	
}
