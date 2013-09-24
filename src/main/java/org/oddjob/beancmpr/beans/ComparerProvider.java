package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.Comparer;

/**
 * Provides {@link Comparer}s.
 * 
 * @author Rob
 *
 */
public interface ComparerProvider {

	/**
	 * Provide a {@link Comparer} for the given property
	 * and type.
	 * 
	 * @param property The name of the property.
	 * @param type The type of the property.
	 * 
	 * @return Never null.
	 */
	public <T> Comparer<T> comparerFor(String property, Class<T> type);
}
