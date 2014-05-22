package org.oddjob.beancmpr.composite;

import org.oddjob.beancmpr.Comparer;

/**
 * A collection of {@link Comparer}s where the comparer can be retrieved
 * by type.
 * 
 * @author rob
 *
 */
public interface ComparersByType {

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
